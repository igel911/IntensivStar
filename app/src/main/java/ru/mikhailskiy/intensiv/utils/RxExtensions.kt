package ru.mikhailskiy.intensiv.utils

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.subscribeOnIoObserveOnMain(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun Completable.subscribeOnIoObserveOnMain(): Completable =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.showProgressBarOnLoad(progressBar: View, viewToHide: View): Single<T> =
    this.doOnSubscribe {
        viewToHide.visibility = GONE
        progressBar.visibility = VISIBLE
    }
        .doFinally {
            viewToHide.visibility = VISIBLE
            progressBar.visibility = GONE
        }