package com.moviedatabase.data.repository

import com.moviedatabase.database.entity.MovieEntity
import com.moviedatabase.movieapi.dto.MovieResponseDto
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getTrending(): Flow<List<MovieEntity>>

    fun getNowPlaying(): Flow<List<MovieEntity>>

    fun getBookmarks(): Flow<List<MovieEntity>>

    suspend fun refreshTrending()

    suspend fun refreshNowPlaying()

    suspend fun bookmark(id: Int, state: Boolean)

    suspend fun search(query: String): MovieResponseDto
}