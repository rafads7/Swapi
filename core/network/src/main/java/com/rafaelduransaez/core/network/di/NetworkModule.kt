package com.rafaelduransaez.core.network.di

import com.rafaelduransaez.core.network.BuildConfig
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

    @Provides
    @Singleton
    @SwapiApiUrl
    fun provideApiUrl(): String = BuildConfig.BACKEND_SWAPI_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        HttpLoggingInterceptor().run {
            level = HttpLoggingInterceptor.Level.BODY
            (OkHttpClient.Builder()
                .addInterceptor(this)
                .build())
        }

    @Provides
    @Singleton
    @SwapiRetrofitClient
    fun provideSwapiRetrofitClient(
        @SwapiApiUrl baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}