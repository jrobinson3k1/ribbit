package com.rebbit.app.ui.subreddit

import android.arch.lifecycle.ViewModel
import com.rebbit.data.api.SubredditClient
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class SubredditViewModel(client: SubredditClient, subreddit: String, ioScheduler: Scheduler, uiScheduler: Scheduler) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val result by lazy { client.getHot(subreddit, ioScheduler, uiScheduler, disposables, 30) }

    val posts = result.pagedList
    val networkState = result.networkState
    val refreshState = result.refreshState

    override fun onCleared() {
        disposables.clear()
    }

    fun retry() {
        result.retry.invoke()
    }

    fun refresh() {
        result.refresh.invoke()
    }
}