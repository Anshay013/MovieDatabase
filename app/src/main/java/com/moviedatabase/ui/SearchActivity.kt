package com.moviedatabase.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviedatabase.databinding.ActivitySearchBinding
import com.moviedatabase.data.viewmodel.SearchViewModel
import com.moviedatabase.ui.adapter.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val vm: SearchViewModel by viewModels()

    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        observeResults()
        setupSearch()
    }

    private fun setupRecycler() {
        adapter = SearchAdapter(
            onBookmarkClick = { vm.toggleBookmark(it) },
            onMovieClick = { 
                Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
            }
        )
        binding.searchRecycler.layoutManager = LinearLayoutManager(this)
        binding.searchRecycler.adapter = adapter
    }

    private fun observeResults() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.results.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun setupSearch() {
        binding.searchInput.addTextChangedListener {
            vm.updateQuery(it.toString())
        }
    }
}