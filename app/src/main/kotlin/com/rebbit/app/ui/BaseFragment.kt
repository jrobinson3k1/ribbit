package com.rebbit.app.ui

import android.support.v4.app.Fragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

abstract class BaseFragment: Fragment() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    protected fun <T> Observable<T>.safeSubscribe(onNext: (T) -> Unit) {
        subscribe(onNext).addTo(compositeDisposable)
    }
}