package com.moviedatabase.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviedatabase.data.repository.MovieRepository
import com.moviedatabase.database.entity.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    private val query = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val results = query
        .debounce(500)
        .flatMapLatest { text ->
            if (text.isBlank()) {
                flowOf(emptyList())
            } else {
                combine(
                    flow { 
                        try {
                            emit(repo.search(text).results)
                        } catch (e: Exception) {
                            emit(emptyList())
                        }
                    },
                    repo.getBookmarks()
                ) { dtoResults, bookmarks ->
                    val bookmarkedIds = bookmarks.map { it.id }.toSet()
                    dtoResults.map { dto ->
                        MovieEntity(
                            id = dto.id,
                            title = dto.title,
                            overview = dto.overview,
                            poster = dto.poster_path,
                            backdrop = dto.backdrop_path,
                            rating = dto.vote_average,
                            releaseDate = dto.release_date,
                            category = "search",
                            bookmarked = dto.id in bookmarkedIds
                        )
                    }
                }
            }
        }
        .flowOn(Dispatchers.IO)

    fun updateQuery(text: String) {
        query.value = text
    }

    fun toggleBookmark(movie: MovieEntity) {
        viewModelScope.launch {
            repo.bookmark(movie.id, !movie.bookmarked)
        }
    }
}