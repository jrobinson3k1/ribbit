package com.rebbit.data.source

import android.arch.paging.DataSource
import android.arch.paging.ItemKeyedDataSource
import com.rebbit.data.api.SubredditApi
import com.rebbit.data.model.NetworkState
import com.rebbit.data.model.Post
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject

class SubredditDataSource(private val api: SubredditApi,
                          private val subreddit: String,
                          private val retryScheduler: Scheduler,
                          private val compositeDisposable: CompositeDisposable) : ItemKeyedDataSource<String, Post>() {

    private var retry: (() -> Any)? = null

    val networkState = BehaviorSubject.create<NetworkState>()
    val initialLoadState = BehaviorSubject.create<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<Post>) {
        api.getHot(subreddit = subreddit, limit = params.requestedLoadSize)
                .map { it.data.children.map { it.data } }
                .doOnSubscribe {
                    networkState.onNext(NetworkState.LOADING)
                    initialLoadState.onNext(NetworkState.LOADING)
                }
                .doFinally {
                    networkState.onNext(NetworkState.LOADED)
                    initialLoadState.onNext(NetworkState.LOADED)
                }
                .subscribe(
                        { posts -> callback.onResult(posts) },
                        { throwable -> networkState.onNext(NetworkState.error(throwable.message)) }
                ).addTo(compositeDisposable)
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<Post>) {
        api.getHot(subreddit = subreddit, after = params.key, limit = params.requestedLoadSize)
                .map { it.data.children.map { it.data } }
                .doOnSubscribe { networkState.onNext(NetworkState.LOADING) }
                .doFinally { networkState.onNext(NetworkState.LOADED) }
                .subscribe(
                        { posts -> callback.onResult(posts) },
                        { throwable -> networkState.onNext(NetworkState.error(throwable.message)) }
                )
                .addTo(compositeDisposable)
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<Post>) {
        // unused
    }

    override fun getKey(item: Post): String = item.name

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryScheduler.scheduleDirect { it.invoke() }
        }
    }

    class Factory(
            private val api: SubredditApi,
            private val subreddit: String,
            private val retryScheduler: Scheduler,
            private val compositeDisposable: CompositeDisposable) : DataSource.Factory<String, Post>() {

        val sourceLiveData = BehaviorSubject.create<SubredditDataSource>()

        override fun create(): DataSource<String, Post> {
            val source = SubredditDataSource(api, subreddit, retryScheduler, compositeDisposable)
            sourceLiveData.onNext(source)
            return source
        }
    }
}