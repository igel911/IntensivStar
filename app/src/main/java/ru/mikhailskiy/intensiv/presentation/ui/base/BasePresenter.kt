package ru.mikhailskiy.intensiv.presentation.ui.base

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V> {

    protected var view: V? = null
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun attachView(view: V) {
        this.view = view
    }

    fun detachView() {
        this.view = null
        compositeDisposable.clear()
    }
}