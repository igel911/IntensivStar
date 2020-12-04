package ru.mikhailskiy.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.mikhailskiy.intensiv.BuildConfig.IMAGE_URL

data class Actor(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
) {
    @SerializedName("profile_path")
    var posterPath: String? = null
        get() = IMAGE_URL.plus(field)
}