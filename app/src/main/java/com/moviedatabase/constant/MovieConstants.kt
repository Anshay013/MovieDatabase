package com.moviedatabase.constant

import com.example.moviedatabase.BuildConfig

object MovieConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    val API_KEY = BuildConfig.MOVIE_API_KEY

    const val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
}