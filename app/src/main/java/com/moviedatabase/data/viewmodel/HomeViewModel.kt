package com.moviedatabase.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviedatabase.data.repository.MovieRepository
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
            SharingStarted.Lazily,
            emptyList())

    val nowPlaying = repo.getNowPlaying()
        .stateIn(viewModelScope,
            SharingStarted.Lazily,
            emptyList())

    init {
        refresh()
    }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        repo.refreshTrending()
        repo.refreshNowPlaying()
    }

    fun bookmark(id: Int, state: Boolean) =
        viewModelScope.launch {
            repo.bookmark(id, state)
        }
}