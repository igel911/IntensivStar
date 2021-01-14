package ru.mikhailskiy.intensiv.data.dto

import com.google.gson.annotations.SerializedName
import ru.mikhailskiy.intensiv.data.dto.ActorDto

data class CreditsResponseDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("cast")
    val actors: List<ActorDto>?
)