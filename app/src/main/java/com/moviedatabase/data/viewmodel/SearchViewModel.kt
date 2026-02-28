package com.moviedatabase.data.viewmodel

import androidx.lifecycle.ViewModel
import com.moviedatabase.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    private val query = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val results = query
        .debounce(500)
        .filter { it.isNotBlank() }
        .flatMapLatest {
            flow {
                emit(repo.search(it).results)
            }
        }
        .flowOn(Dispatchers.IO)

    fun updateQuery(text: String) {
        query.value = text
    }
}