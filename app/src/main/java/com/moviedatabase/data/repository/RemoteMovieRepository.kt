package com.moviedatabase.data.repository

import com.moviedatabase.constant.MovieConstants
import com.moviedatabase.movieapi.api.MovieApi
import jakarta.inject.Inject

class RemoteMovieRepository @Inject constructor(
    private val api: MovieApi
) {

    suspend fun getTrending() =
        api.getTrending(MovieConstants.API_KEY)

    suspend fun getNowPlaying() =
        api.getNowPlaying(MovieConstants.API_KEY)

    suspend fun search(query: String) =
        api.searchMovies(query, MovieConstants.API_KEY)
}