package com.moviedatabase.data.repository

import android.util.Log
import com.moviedatabase.data.mapper.toEntity
import com.moviedatabase.database.entity.MovieEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val local: LocalMovieRepository,
    private val remote: RemoteMovieRepository
) : MovieRepository {

    override fun getTrending() = local.getTrending()

    override fun getNowPlaying() = local.getNowPlaying()

    override fun getBookmarks() = local.getBookmarks()

    override fun searchLocal(query: String) = local.searchLocal(query)

    override suspend fun getMovieById(id: Int): MovieEntity? = local.getMovieById(id)

    override suspend fun refreshTrending() {
        try {
            val response = remote.getTrending()
            local.saveMovies(
                "trending",
                response.results.map { it.toEntity("trending") }
            )
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error refreshing trending movies", e)
        }
    }

    override suspend fun refreshNowPlaying() {
        try {
            val response = remote.getNowPlaying()
            local.saveMovies(
                "now",
                response.results.map { it.toEntity("now") }
            )
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error refreshing now playing movies", e)
        }
    }

    override suspend fun toggleBookmark(movie: MovieEntity) {
        val updated = movie.copy(bookmarked = !movie.bookmarked)
        local.upsertMovie(updated)
    }

    override suspend fun search(query: String) =
        remote.search(query)
}