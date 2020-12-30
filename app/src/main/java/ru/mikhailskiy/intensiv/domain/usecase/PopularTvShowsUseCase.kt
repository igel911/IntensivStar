package ru.mikhailskiy.intensiv.domain.usecase

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.vo.TvShow
import ru.mikhailskiy.intensiv.domain.repository.TvShowsRepository
import ru.mikhailskiy.intensiv.utils.SingleThreadTransformer

class PopularTvShowsUseCase(private val repository: TvShowsRepository) {

    fun getTvShows(): Single<List<TvShow>> {
        return repository.getTvShows()
            .compose(SingleThreadTransformer<List<TvShow>>())
    }
}