package com.moviedatabase.movieapi.api

import com.moviedatabase.movieapi.dto.MovieResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("trending/movie/day")
    suspend fun getTrending(
        @Query("api_key") key: String
    ): MovieResponseDto

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") key: String
    ): MovieResponseDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") key: String
    ): MovieResponseDto
}