package ru.mikhailskiy.intensiv.data.dto

import com.google.gson.annotations.SerializedName
import ru.mikhailskiy.intensiv.BuildConfig.IMAGE_URL

data class MovieDto (
    @SerializedName("adult")
    val isAdult: Boolean,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val genreIds: List<Int>,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    @SerializedName("video")
    val video: Boolean?,
    @SerializedName("vote_average")
    val voteAverage: Double?
) {
    val rating: Float
        get() = voteAverage?.div(2)?.toFloat() ?: 0.0f

    @SerializedName("poster_path")
    var posterPath: String? = null
        get() = IMAGE_URL.plus(field)
}
