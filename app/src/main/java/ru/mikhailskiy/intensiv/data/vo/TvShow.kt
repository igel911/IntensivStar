package ru.mikhailskiy.intensiv.data.vo

import ru.mikhailskiy.intensiv.BuildConfig

data class TvShow(
    val id: Int,
    val name: String,
    val voteAverage: Double,
    val posterPath: String
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()

    val posterFullPath: String
        get() = BuildConfig.IMAGE_URL.plus(posterPath)
}