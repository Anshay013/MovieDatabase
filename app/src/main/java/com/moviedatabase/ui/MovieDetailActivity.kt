package com.moviedatabase.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.moviedatabase.R
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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        handleIntent(intent)

        observeMovie()
        setupListeners()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val movieId = intent.getIntExtra("MOVIE_ID", -1)
        if (movieId != -1) {
            viewModel.loadMovie(movieId)
        } else {
            // Handle Deeplink: moviedb://movie/{id}
            intent.data?.lastPathSegment?.toIntOrNull()?.let { id ->
                viewModel.loadMovie(id)
            }
        }
    }

    private fun observeMovie() {
        lifecycleScope.launch {
            viewModel.movie.collect { movie ->
                movie?.let {
                    binding.ivBackdrop.load(MovieConstants.IMAGE_BASE + it.backdrop)
                    binding.tvTitle.text = it.title
                    binding.tvReleaseDate.text = "Release Date: ${it.releaseDate}"
                    binding.ratingBar.rating = (it.rating).toFloat()
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
        
        binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.saved_movies_menu, menu) // Reuse share menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareMovie()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareMovie() {
        val movie = viewModel.movie.value ?: return
        // Dummy deeplink for the bonus task
        val deeplink = "moviedb://movie/${movie.id}"
        val shareText = "Check out this movie: ${movie.title}\n\n${movie.overview}\n\nView details: $deeplink"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Share Movie"))
    }

    private fun updateBookmarkIcon(isBookmarked: Boolean) {
        if (isBookmarked) {
            binding.fabBookmark.setImageResource(android.R.drawable.btn_star_big_on)
            binding.fabBookmark.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.yellow)
            )
        } else {
            binding.fabBookmark.setImageResource(android.R.drawable.btn_star_big_off)
            binding.fabBookmark.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.white)
            )
        }
    }
}