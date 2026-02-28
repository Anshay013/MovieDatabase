package com.moviedatabase.model

data class Movie(
    val id: Int,
    val title: String,
    val poster: String?,
    val rating: Double,
    val bookmarked: Boolean
)