package ru.mikhailskiy.intensiv.data.repository

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.mappers.TvShowMapper
import ru.mikhailskiy.intensiv.data.network.MovieApiClient
import ru.mikhailskiy.intensiv.data.vo.TvShow
import ru.mikhailskiy.intensiv.domain.repository.TvShowsRepository

class PopularTvShowsRepository : TvShowsRepository {

    override fun getTvShows(): Single<List<TvShow>> {
        return MovieApiClient.apiClient.getPopularTvShows()
            .map { TvShowMapper.toValueObject(it) }
    }
}