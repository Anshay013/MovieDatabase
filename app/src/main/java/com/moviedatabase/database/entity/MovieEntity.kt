package com.moviedatabase.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val poster: String?,
    val backdrop: String?,
    val rating: Double,
    val releaseDate: String,
    val category: String,
    val bookmarked: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)