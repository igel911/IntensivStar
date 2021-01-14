package ru.mikhailskiy.intensiv.data.mappers

import ru.mikhailskiy.intensiv.data.dto.TvShowDto
import ru.mikhailskiy.intensiv.data.dto.TvShowsResponseDto
import ru.mikhailskiy.intensiv.data.vo.TvShow

object TvShowMapper {

    fun toValueObject(dto: TvShowsResponseDto): List<TvShow> {
        return dto.results?.map { toValueObject(it) } ?: emptyList()
    }

    private fun toValueObject(dto: TvShowDto): TvShow {
        return TvShow(
            id = dto.id ?: 0,
            name = dto.name ?: "",
            voteAverage = dto.voteAverage ?: 0.0,
            posterPath = dto.posterPath ?: ""
        )
    }
}