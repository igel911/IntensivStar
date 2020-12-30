package ru.mikhailskiy.intensiv.data.dto

import com.google.gson.annotations.SerializedName
import ru.mikhailskiy.intensiv.BuildConfig.IMAGE_URL

data class ActorDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
) {
    @SerializedName("profile_path")
    var posterPath: String? = null
        get() = IMAGE_URL.plus(field)
}