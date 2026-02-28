package com.moviedatabase.movieapi.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponseDto(
    val results: List<MovieDto>
)