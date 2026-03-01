package com.moviedatabase.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedatabase.databinding.ActivitySavedMoviesBinding
import com.moviedatabase.data.viewmodel.SavedMoviesViewModel
import com.moviedatabase.ui.adapter.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedMoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedMoviesBinding
    private val viewModel: SavedMoviesViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        observeSavedMovies()
    }

    private fun setupRecycler() {
        adapter = MovieAdapter(
            onBookmarkClick = { viewModel.toggleBookmark(it) },
            onMovieClick = { 
                val intent = Intent(this, MovieDetailActivity::class.java).apply {
                    putExtra("MOVIE_ID", it.id)
                }
                startActivity(intent)
            }
        )
        binding.rvSavedMovies.layoutManager = GridLayoutManager(this, 3)
        binding.rvSavedMovies.adapter = adapter
    }

    private fun observeSavedMovies() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savedMovies.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
}