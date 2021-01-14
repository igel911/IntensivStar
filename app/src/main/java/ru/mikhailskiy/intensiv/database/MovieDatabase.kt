package ru.mikhailskiy.intensiv.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.mikhailskiy.intensiv.database.models.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movies(): MovieStore

    companion object {
        private const val DB_NAME = "DB_MOVIE"

        fun getDatabase(context: Context) = Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            DB_NAME
        ).build()
    }
}