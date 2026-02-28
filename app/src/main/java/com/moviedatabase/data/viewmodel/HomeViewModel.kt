package com.moviedatabase.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviedatabase.data.repository.MovieRepository
import com.moviedatabase.database.entity.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val trending = repo.getTrending()
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList())

    val nowPlaying = repo.getNowPlaying()
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList())

    init {
        refresh()
    }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        repo.refreshTrending()
        repo.refreshNowPlaying()
    }

    fun toggleBookmark(movie: MovieEntity) =
        viewModelScope.launch {
            repo.bookmark(movie.id, !movie.bookmarked)
        }
}