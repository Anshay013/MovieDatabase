package com.moviedatabase.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviedatabase.databinding.ItemMovieBinding
import com.moviedatabase.constant.MovieConstants
import com.moviedatabase.database.entity.MovieEntity

class MovieAdapter(
    private val onBookmarkClick: (MovieEntity) -> Unit,
    private val onMovieClick: (MovieEntity) -> Unit
) : ListAdapter<MovieEntity, MovieAdapter.MovieVH>(Diff()) {

    inner class MovieVH(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieVH(binding)
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        val movie = getItem(position)

        holder.binding.title.text = movie.title

        holder.binding.poster.load(MovieConstants.IMAGE_BASE + movie.poster)

        holder.binding.bookmark.setImageResource(
            if (movie.bookmarked)
                android.R.drawable.btn_star_big_on
            else
                android.R.drawable.btn_star_big_off
        )

        holder.binding.bookmark.setOnClickListener {
            onBookmarkClick(movie)
        }

        holder.binding.root.setOnClickListener {
            onMovieClick(movie)
        }
    }

    class Diff : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(a: MovieEntity, b: MovieEntity) = a.id == b.id
        override fun areContentsTheSame(a: MovieEntity, b: MovieEntity) = a == b
    }
}