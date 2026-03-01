package com.moviedatabase.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.moviedatabase.databinding.ActivityMovieDetailBinding
import com.moviedatabase.constant.MovieConstants
import com.moviedatabase.data.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra("MOVIE_ID", -1)
        if (movieId != -1) {
            viewModel.loadMovie(movieId)
        }

        observeMovie()
        setupListeners()
    }

    private fun observeMovie() {
        lifecycleScope.launch {
            viewModel.movie.collect { movie ->
                movie?.let {
                    binding.ivBackdrop.load(MovieConstants.IMAGE_BASE + it.backdrop)
                    binding.tvTitle.text = it.title
                    binding.tvReleaseDate.text = "Release Date: ${it.releaseDate}"
                    binding.ratingBar.rating = it.rating.toFloat()
                    binding.tvOverview.text = it.overview
                    
                    updateBookmarkIcon(it.bookmarked)
                }
            }
        }
    }

    private fun setupListeners() {
        binding.fabBookmark.setOnClickListener {
            viewModel.toggleBookmark()
        }
        
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun updateBookmarkIcon(isBookmarked: Boolean) {
        binding.fabBookmark.setImageResource(
            if (isBookmarked) android.R.drawable.btn_star_big_on 
            else android.R.drawable.btn_star_big_off
        )
    }
}