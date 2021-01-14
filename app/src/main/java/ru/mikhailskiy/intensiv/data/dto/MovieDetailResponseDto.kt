package ru.mikhailskiy.intensiv.data.dto

import com.google.gson.annotations.SerializedName

data class MovieDetailResponseDto(
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("genres")
    val genres: List<GenreDto>?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanyDto>?
) {
    val companyNamesString: String
        get() = productionCompanies?.joinToString { it.name ?: "" } ?: ""

    val genresString: String
        get() = genres?.joinToString { it.name ?: "" } ?: ""

    val year: String
        get() = releaseDate?.split("-")?.get(0) ?: ""
}