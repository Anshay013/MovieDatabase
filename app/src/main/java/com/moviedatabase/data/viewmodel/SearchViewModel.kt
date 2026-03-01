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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val results = _query
        .flatMapLatest { text ->
            if (text.isBlank()) {
                flowOf(emptyList())
            } else {
                // Combine local search (instant) with remote search (debounced)
                combine(
                    repo.searchLocal(text),
                    getRemoteSearchFlow(text),
                    repo.getBookmarks()
                ) { local, remote, bookmarks ->
                    val bookmarkedIds = bookmarks.map { it.id }.toSet()
                    // Merge local and remote, then apply bookmark status
                    (local + remote)
                        .distinctBy { it.id }
                        .map { it.copy(bookmarked = it.id in bookmarkedIds) }
                }
            }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun getRemoteSearchFlow(text: String) = flowOf(text)
        .debounce(600) // if within 600 ms user doesn't type a thing we fetch latest data from network
        .flatMapLatest { queryText ->
            flow {
                try {
                    val dtoResults = repo.search(queryText).results
                    val entities = dtoResults.map { dto ->
                        MovieEntity(
                            id = dto.id,
                            title = dto.title,
                            overview = dto.overview,
                            poster = dto.poster_path,
                            backdrop = dto.backdrop_path,
                            rating = dto.vote_average,
                            releaseDate = dto.release_date,
                            category = "search"
                        )
                    }
                    emit(entities)
                } catch (e: Exception) {
                    emit(emptyList<MovieEntity>())
                }
            }
        }

    fun updateQuery(text: String) {
        _query.value = text
    }

    fun toggleBookmark(movie: MovieEntity) {
        viewModelScope.launch {
            repo.bookmark(movie.id, !movie.bookmarked)
        }
    }
}