package ru.mikhailskiy.intensiv.presentation.ui.tvshows

import io.reactivex.Observable
import ru.mikhailskiy.intensiv.data.vo.TvShow
import ru.mikhailskiy.intensiv.domain.usecase.PopularTvShowsUseCase
import ru.mikhailskiy.intensiv.presentation.ui.base.BasePresenter
import timber.log.Timber

class TvShowsPresenter(private val useCase: PopularTvShowsUseCase) :
    BasePresenter<TvShowsPresenter.TvShowView>() {

    fun getTvShows() {
        compositeDisposable.add(useCase.getTvShows()
            .doOnSubscribe { view?.showLoading() }
            .flatMapObservable { Observable.fromIterable(it) }
            .map {
                TvShowItem(it) { tvShow ->
                    view?.openTvShowDetails(
                        tvShow
                    )
                }
            }
            .toList()
            .doFinally { view?.hideLoading() }
            .subscribe(
                { view?.showTvShows(it) },
                {
                    Timber.e(it.toString())
                    view?.showError()
                }
            )
        )
    }

    interface TvShowView {
        fun showTvShows(tvShows: List<TvShowItem>)
        fun openTvShowDetails(tvShow: TvShow)
        fun showLoading()
        fun hideLoading()
        fun showEmptyTvShows()
        fun showError()
    }
}