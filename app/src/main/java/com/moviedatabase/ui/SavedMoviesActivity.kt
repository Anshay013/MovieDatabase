package com.moviedatabase.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedatabase.R
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
    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbarSaved)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }

        setupRecycler()
        observeState()
    }

    private fun setupRecycler() {
        adapter = MovieAdapter(
            onBookmarkClick = { viewModel.toggleBookmark(it) },
            onMovieClick = {
                val intent = Intent(this, MovieDetailActivity::class.java).apply {
                    putExtra("MOVIE_ID", it.id)
                }
                startActivity(intent)
            },
            onLongClick = {
                if (actionMode == null) {
                    viewModel.setSelectionMode(true)
                    actionMode = startSupportActionMode(actionModeCallback)
                }
                viewModel.toggleSelection(it.id)
            }
        )
        binding.rvSavedMovies.layoutManager = GridLayoutManager(this, 3)
        binding.rvSavedMovies.adapter = adapter
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.movies)
                    adapter.updateSelection(state.selectedIds, state.isSelectionMode)
                    
                    if (state.isSelectionMode) {
                        if (actionMode == null) {
                            actionMode = startSupportActionMode(actionModeCallback)
                        }
                        updateActionModeTitle(state.selectedIds.size)
                    } else {
                        actionMode?.finish()
                        actionMode = null
                    }
                }
            }
        }
    }

    private fun updateActionModeTitle(count: Int) {
        val titleView = actionMode?.customView as? TextView 
            ?: LayoutInflater.from(this).inflate(R.layout.action_mode_title, null) as TextView
        
        titleView.text = if (count > 0) "$count selected" else "Select items"
        actionMode?.customView = titleView
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.saved_movies_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.action_share -> {
                    shareSelectedMovies()
                    viewModel.setSelectionMode(false)
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            viewModel.setSelectionMode(false)
            actionMode = null
        }
    }

    private fun shareSelectedMovies() {
        val selectedMovies = adapter.getSelectedMovies()
        if (selectedMovies.isEmpty()) return

        val shareText = selectedMovies.joinToString("\n\n") { movie ->
            "Title: ${movie.title}\nOverview: ${movie.overview}\nRating: ${movie.rating}"
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "My Saved Movies")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Share Movies via"))
    }
}