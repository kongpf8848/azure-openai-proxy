package io.github.kongpf8848.azure.okhttp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpManager{

    val client: OkHttpClient

    init {
        val logInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        client = OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .build()
    }

}