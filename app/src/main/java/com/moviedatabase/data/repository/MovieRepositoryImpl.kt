package com.moviedatabase.data.repository

import com.moviedatabase.data.mapper.toEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val local: LocalMovieRepository,
    private val remote: RemoteMovieRepository
) : MovieRepository {

    override fun getTrending() = local.getTrending()

    override fun getNowPlaying() = local.getNowPlaying()

    override fun getBookmarks() = local.getBookmarks()

    override fun searchLocal(query: String) = local.searchLocal(query)

    override suspend fun refreshTrending() {
        val response = remote.getTrending()
        local.saveMovies(
            "trending",
            response.results.map { it.toEntity("trending") }
        )
    }

    override suspend fun refreshNowPlaying() {
        val response = remote.getNowPlaying()
        local.saveMovies(
            "now",
            response.results.map { it.toEntity("now") }
        )
    }

    override suspend fun bookmark(id: Int, state: Boolean) {
        local.bookmark(id, state)
    }

    override suspend fun search(query: String) =
        remote.search(query)
}