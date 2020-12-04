package ru.mikhailskiy.intensiv.data

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("genres")
    val genres: List<Genre>?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>?
) {
    val companyNamesString: String
    get() = productionCompanies?.joinToString { it.name ?: "" } ?: ""

    val genresString: String
    get() = genres?.joinToString { it.name ?: "" } ?: ""

    val year: String
    get() = releaseDate?.split("-")?.get(0) ?: ""
}