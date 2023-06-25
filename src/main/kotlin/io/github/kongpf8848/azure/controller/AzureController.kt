package io.github.kongpf8848.azure.controller

import io.github.kongpf8848.azure.config.AzureConfig
import io.github.kongpf8848.azure.okhttp.OkHttpManager
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.observers.DisposableObserver
import lombok.extern.slf4j.Slf4j
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@Slf4j
class AzureController {

    private val log = LoggerFactory.getLogger(AzureController::class.java)

    @Autowired
    lateinit var config: AzureConfig

    @PostMapping(path = ["/v1/chat/completions"])
    fun chatCompletions(request: HttpServletRequest, response: HttpServletResponse): SseEmitter {
        val sseEmitter = SseEmitter(0L)

        sseEmitter.onTimeout {
            log.info("==========sseEmitter onTimeout")
        }
        sseEmitter.onError() {
            log.info("==========sseEmitter onError")
        }
        sseEmitter.onCompletion {
            log.info("==========sseEmitter onCompletion")
        }

        val requestContent = String((request.inputStream as InputStream).readBytes())

        log.info("request method:{}", request.method)
        log.info("request ContentType:{}", request.contentType)
        log.info("request.url:{}", request.requestURL)
        log.info("request content:{}", requestContent)
        val key = request.getHeader("Authorization").replace("Bearer", "").trim { it <= ' ' }
        log.info("request authorization key:{}", key)
        val url = "${config.endpoint}openai/deployments/${config.deployment_id}/chat/completions?api-version=${config.api_version}"
        val requestBody = RequestBody.create(request.contentType.toMediaType(), requestContent)
        val okhttpRequest = Request.Builder()
            .url(url).method(request.method, requestBody)
            .addHeader("api-key", key)
            .build()

        OkHttpManager.client.newCall(okhttpRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                getObservable(response).subscribe(object : DisposableObserver<String>() {
                    override fun onNext(data: String) {
                        log.info("==========onNext:$data")
                        try {
                            sseEmitter.send(data)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (data == " [DONE]") {
                            onComplete()
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        sseEmitter.complete()
                        dispose()
                    }

                    override fun onComplete() {
                        sseEmitter.complete()
                        dispose()
                    }
                })
            }
        })
        return sseEmitter
    }

    fun getObservable(response: Response): Observable<String> {
        val ob1 = Observable.interval(50, TimeUnit.MILLISECONDS)
        val ob2 = Observable.create { emitter: ObservableEmitter<String> ->
            if (response.isSuccessful) {
                val inputStream = response.body!!.byteStream()
                val reader = BufferedReader(InputStreamReader(inputStream))
                var str: String
                while (true) {
                    str = reader.readLine()
                    if (!str.isNullOrEmpty()) {
                        if (str.startsWith("data:")) {
                            val data = str.substring(5)
                            emitter.onNext(data)
                            if (data == " [DONE]") {
                                break
                            }
                        }
                    }
                }
                try {
                    reader.close()
                    inputStream.close()
                    response.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                log.info("onFailure() called with: message = [" + response.message + "]")
                emitter.onError(RuntimeException(response.message))
            }
        }
        return Observable.zip(ob1, ob2) { _, s: String -> s }
    }
}