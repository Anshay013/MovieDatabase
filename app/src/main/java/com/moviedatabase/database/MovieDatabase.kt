package com.moviedatabase.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moviedatabase.database.dao.MovieDao
import com.moviedatabase.database.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}