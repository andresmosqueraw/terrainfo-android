package com.coplanin.terrainfo.di

import com.coplanin.terrainfo.data.repository.AuthService
import com.coplanin.terrainfo.data.repository.CommonDataService
import com.coplanin.terrainfo.data.utils.GsonUtils
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
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides @Singleton
    fun provideOkHttp(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val newRequest = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ConstantsModule.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonUtils.getGson()))
            .build()

    @Provides @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides @Singleton
    fun provideCommonDataService(retrofit: Retrofit): CommonDataService =
        retrofit.create(CommonDataService::class.java)
}