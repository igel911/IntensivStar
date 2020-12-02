package ru.mikhailskiy.intensiv.data

import com.google.gson.annotations.SerializedName

data class TvShow(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?
) {
    val rating: Float
        get() = voteAverage?.div(2)?.toFloat() ?: 0.0f

    @SerializedName("poster_path")
    var posterPath: String? = null
        get() = "https://image.tmdb.org/t/p/w500$field"
}