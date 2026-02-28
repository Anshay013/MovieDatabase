package com.moviedatabase.data.mapper

import com.moviedatabase.database.entity.MovieEntity
import com.moviedatabase.movieapi.dto.MovieDto

fun MovieDto.toEntity(category: String) =
    MovieEntity(
        id = id,
        title = title,
        overview = overview,
        poster = poster_path,
        backdrop = backdrop_path,
        rating = vote_average,
        releaseDate = release_date,
        category = category
    )