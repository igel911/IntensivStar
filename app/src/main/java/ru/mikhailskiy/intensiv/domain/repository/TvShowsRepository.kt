package ru.mikhailskiy.intensiv.domain.repository

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.vo.TvShow

interface TvShowsRepository {
    fun getTvShows(): Single<List<TvShow>>
}