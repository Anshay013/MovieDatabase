package com.moviedatabase.data.repository

import com.moviedatabase.database.dao.MovieDao
import com.moviedatabase.database.entity.MovieEntity
import jakarta.inject.Inject

class LocalMovieRepository @Inject constructor(
    private val dao: MovieDao
) {

    fun getTrending() = dao.getMovies("trending")

    fun getNowPlaying() = dao.getMovies("now")

    fun getBookmarks() = dao.getBookmarks()

    suspend fun saveMovies(category: String, movies: List<MovieEntity>) {
        dao.clearCategory(category)
        dao.insertAll(movies)
    }

    suspend fun bookmark(id: Int, state: Boolean) =
        dao.bookmark(id, state)
}