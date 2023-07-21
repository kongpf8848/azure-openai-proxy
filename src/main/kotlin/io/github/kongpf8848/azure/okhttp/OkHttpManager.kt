package io.github.kongpf8848.azure.okhttp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpManager {

    val client: OkHttpClient by lazy {
        val logInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .build()
    }

}