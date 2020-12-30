package ru.mikhailskiy.intensiv.data.dto

import com.google.gson.annotations.SerializedName
import ru.mikhailskiy.intensiv.BuildConfig.IMAGE_URL

data class TvShowDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("poster_path")
    var posterPath: String?
)