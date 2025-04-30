package com.coplanin.terrainfo.di

import com.coplanin.terrainfo.data.repository.ApiClient
import com.coplanin.terrainfo.data.repository.AuthService
import com.coplanin.terrainfo.data.repository.CommonDataService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideOkHttp() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)   // o constante aquí
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides fun authService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    fun commonDataService(retrofit: Retrofit): CommonDataService =
        retrofit.create(CommonDataService::class.java)
}