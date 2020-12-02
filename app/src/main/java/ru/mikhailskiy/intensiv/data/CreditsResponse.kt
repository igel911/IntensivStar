package ru.mikhailskiy.intensiv.data

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("cast")
    val actors: List<Actor>?
)