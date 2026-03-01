package com.moviedatabase.data.repository

import com.moviedatabase.database.entity.MovieEntity
import com.moviedatabase.movieapi.dto.MovieResponseDto
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getTrending(): Flow<List<MovieEntity>>

    fun getNowPlaying(): Flow<List<MovieEntity>>

    fun getBookmarks(): Flow<List<MovieEntity>>

    fun searchLocal(query: String): Flow<List<MovieEntity>>

    suspend fun getMovieById(id: Int): MovieEntity?

    suspend fun refreshTrending()

    suspend fun refreshNowPlaying()

    suspend fun toggleBookmark(movie: MovieEntity)

    suspend fun search(query: String): MovieResponseDto
}