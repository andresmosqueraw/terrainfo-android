package com.coplanin.terrainfo.data.repository

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object ApiClient {
    // private const val BASE_URL = "http://localhost:8000/"
    // const val BASE_URL = "http://10.0.2.2:8081/" // PARA EMULADORES ANDROID
    const val BASE_URL = "http://3.133.21.126:8081/"

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val original = chain.request()
            val newRequest = original.newBuilder()
                .header("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        }
        .build()
}