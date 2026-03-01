package com.moviedatabase.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviedatabase.data.repository.MovieRepository
import com.moviedatabase.database.entity.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movie = MutableStateFlow<MovieEntity?>(null)
    val movie: StateFlow<MovieEntity?> = _movie

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            _movie.value = repository.getMovieById(movieId)
        }
    }

    fun toggleBookmark() {
        val currentMovie = _movie.value ?: return
        viewModelScope.launch {
            repository.toggleBookmark(currentMovie)
            // Refresh local state
            loadMovie(currentMovie.id)
        }
    }
}