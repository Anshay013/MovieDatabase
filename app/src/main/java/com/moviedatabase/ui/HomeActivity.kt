package com.moviedatabase.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedatabase.databinding.ActivityHomeBinding
import com.moviedatabase.data.viewmodel.HomeViewModel
import com.moviedatabase.database.entity.MovieEntity
import com.moviedatabase.ui.adapter.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val vm: HomeViewModel by viewModels()

    private lateinit var trendingAdapter: MovieAdapter
    private lateinit var nowAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapters()
        observeMovies()
        setupInteractions()
    }

    private fun setupAdapters() {

        trendingAdapter = MovieAdapter(
            onBookmarkClick = { vm.toggleBookmark(it) },
            onMovieClick = { openDetails(it) }
        )

        nowAdapter = MovieAdapter(
            onBookmarkClick = { vm.toggleBookmark(it) },
            onMovieClick = { openDetails(it) }
        )

        binding.trendingRecycler.apply {
            layoutManager = LinearLayoutManager(
                this@HomeActivity,
                RecyclerView.HORIZONTAL,
                false
            )
            adapter = trendingAdapter
        }

        binding.nowRecycler.apply {
            layoutManager = LinearLayoutManager(
                this@HomeActivity,
                RecyclerView.HORIZONTAL,
                false
            )
            adapter = nowAdapter
        }
    }

    private fun observeMovies() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.trending.collect {
                        trendingAdapter.submitList(it)
                    }
                }
                launch {
                    vm.nowPlaying.collect {
                        nowAdapter.submitList(it)
                    }
                }
            }
        }
    }

    private fun setupInteractions() {

        // Search navigation
        binding.searchBar.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        // manual refresh button, fetch everything from API and
        binding.btnRefresh.setOnClickListener {
            vm.refresh()
        }

        // pull to refresh just like above
        binding.swipeRefresh.setOnRefreshListener {
            vm.refresh()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun openDetails(movie: MovieEntity) {
        Toast.makeText(this, movie.title, Toast.LENGTH_SHORT).show()
    }
}