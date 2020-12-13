package ru.mikhailskiy.intensiv.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val overview: String,
    val posterPath: String,
    val title: String,
    val rating: Float
)