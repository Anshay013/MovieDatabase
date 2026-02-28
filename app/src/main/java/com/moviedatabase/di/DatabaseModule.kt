package com.moviedatabase.di

import android.content.Context
import androidx.room.Room
import com.moviedatabase.database.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movies.db"
        ).build()

    @Provides
    fun provideDao(db: MovieDatabase) = db.movieDao()
}