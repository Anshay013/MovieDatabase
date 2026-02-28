package com.moviedatabase.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moviedatabase.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE category = :category")
    fun getMovies(category: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE bookmarked = 1")
    fun getBookmarks(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("DELETE FROM movies WHERE category = :category")
    suspend fun clearCategory(category: String)

    @Query("UPDATE movies SET bookmarked = :state WHERE id = :id")
    suspend fun bookmark(id: Int, state: Boolean)
}