package com.rebbit.data.api

import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.rebbit.data.model.LiveListing
import com.rebbit.data.model.Post
import com.rebbit.data.source.SubredditDataSource
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class SubredditClient(private val api: SubredditApi) {

    fun getHot(subreddit: String,
               ioScheduler: Scheduler,
               uiScheduler: Scheduler,
               compositeDisposable: CompositeDisposable,
               pageSize: Int = 25): LiveListing<Post> {
        val sourceFactory = SubredditDataSource.Factory(api, subreddit, ioScheduler, compositeDisposable)
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPageSize(pageSize)
                .build()
        val pagedList = RxPagedListBuilder(sourceFactory, pagedListConfig)
                .setFetchScheduler(ioScheduler)
                .setNotifyScheduler(uiScheduler)
                .buildObservable()

        return LiveListing(
                pagedList = pagedList,
                networkState = sourceFactory.sourceLiveData.switchMap { it.networkState }.observeOn(uiScheduler),
                refreshState = sourceFactory.sourceLiveData.switchMap { it.initialLoadState }.observeOn(uiScheduler),
                retry = { sourceFactory.sourceLiveData.value?.retryAllFailed() },
                refresh = { sourceFactory.sourceLiveData.value?.invalidate() }
        )
    }
}