package com.coplanin.terrainfo.data.repository

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    private val gson = GsonBuilder()
        // Configura Gson si lo requieres
        .create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}