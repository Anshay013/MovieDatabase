package com.moviedatabase.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.moviedatabase.constant.MovieConstants
import com.moviedatabase.movieapi.api.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideJson() = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    fun provideRetrofit(json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(MovieConstants.BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()

    @Provides
    fun provideApi(retrofit: Retrofit): MovieApi =
        retrofit.create(MovieApi::class.java)
}