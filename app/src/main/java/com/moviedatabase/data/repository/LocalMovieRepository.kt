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

    fun searchLocal(query: String) = dao.searchLocal(query)

    suspend fun saveMovies(category: String, movies: List<MovieEntity>) {
        dao.upsertMovies(movies)
    }

    suspend fun bookmark(id: Int, state: Boolean) =
        dao.bookmark(id, state)
}