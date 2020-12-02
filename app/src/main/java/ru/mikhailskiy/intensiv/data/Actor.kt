package ru.mikhailskiy.intensiv.data

import com.google.gson.annotations.SerializedName

data class Actor(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
) {
    @SerializedName("profile_path")
    var posterPath: String? = null
        get() = "https://image.tmdb.org/t/p/w500$field"
}