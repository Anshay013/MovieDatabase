package com.moviedatabase.data.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviedatabase.data.repository.MovieRepository
import com.moviedatabase.database.entity.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SavedMoviesUiState(
    val movies: List<MovieEntity> = emptyList(),
    val selectedIds: Set<Int> = emptySet(),
    val isSelectionMode: Boolean = false
)

@HiltViewModel
class SavedMoviesViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val isSelectionModeFlow = savedStateHandle.getStateFlow("selection_mode", false)
    private val selectedIdsListFlow = savedStateHandle.getStateFlow("selected_ids", emptyList<Int>())

    val uiState: StateFlow<SavedMoviesUiState> = combine(
        repository.getBookmarks(),
        isSelectionModeFlow,
        selectedIdsListFlow
    ) { movies, isSelectionMode, selectedIdsList ->
        SavedMoviesUiState(
            movies = movies,
            selectedIds = selectedIdsList.toSet(),
            isSelectionMode = isSelectionMode
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SavedMoviesUiState()
    )

    fun toggleBookmark(movie: MovieEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(movie)
        }
    }

    fun setSelectionMode(enabled: Boolean) {
        if (savedStateHandle.get<Boolean>("selection_mode") == enabled) return
        savedStateHandle["selection_mode"] = enabled
        if (!enabled) {
            savedStateHandle["selected_ids"] = emptyList<Int>()
        }
    }

    fun toggleSelection(movieId: Int) {
        val currentList = selectedIdsListFlow.value.toMutableList()
        if (currentList.contains(movieId)) {
            currentList.remove(movieId)
        } else {
            currentList.add(movieId)
        }
        
        savedStateHandle["selected_ids"] = currentList
        
        if (currentList.isEmpty()) {
            setSelectionMode(false)
        } else if (!isSelectionModeFlow.value) {
            setSelectionMode(true)
        }
    }
}