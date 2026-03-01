package com.moviedatabase.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.moviedatabase.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE category = :category")
    fun getMovies(category: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE bookmarked = 1")
    fun getBookmarks(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    fun searchLocal(query: String): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(movies: List<MovieEntity>): List<Long>

    @Query("UPDATE movies SET title = :title, overview = :overview, poster = :poster, backdrop = :backdrop, rating = :rating, releaseDate = :releaseDate, category = :category WHERE id = :id")
    suspend fun updateMovie(id: Int, title: String, overview: String, poster: String?, backdrop: String?, rating: Double, releaseDate: String, category: String)

    @Transaction
    suspend fun upsertMovies(movies: List<MovieEntity>) {
        val insertResults = insertAll(movies)
        for (i in insertResults.indices) {
            if (insertResults[i] == -1L) {
                val movie = movies[i]
                updateMovie(movie.id, movie.title, movie.overview, movie.poster, movie.backdrop, movie.rating, movie.releaseDate, movie.category)
            }
        }
    }

    @Query("UPDATE movies SET bookmarked = :state WHERE id = :id")
    suspend fun bookmark(id: Int, state: Boolean)
}