package com.rebbit.app.ui.subreddit

import com.rebbit.data.api.ListingClient
import com.rebbit.data.model.Link
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

interface SubredditPresenter {
    fun bind(view: SubredditView)
    fun onRefresh()
    fun unBind()
}

class SubredditPresenterImpl(private val listingClient: ListingClient, private val subreddit: String) : SubredditPresenter {

    private var view: SubredditView? = null
    private val disposables = CompositeDisposable()

    override fun bind(view: SubredditView) {
        this.view = view
        onRefresh()
    }

    override fun onRefresh() {
        getLinks(
                subreddit,
                safeView()::showLinks,
                { throwable -> safeView().onError(throwable.message ?: "Unknown error") }
        )
    }

    override fun unBind() {
        disposables.dispose()
        view = null
    }

    private fun getLinks(subreddit: String, onSuccess: (List<Link>) -> Unit, onError: (Throwable) -> Unit) {
        listingClient.getSubreddit(subreddit)
                .map { it.data.children.map { it.data } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { safeView().setRefreshing(true) }
                .doFinally { safeView().setRefreshing(false) }
                .subscribe(onSuccess, onError)
                .addTo(disposables)
    }

    private fun safeView() = view?.let { it } ?: object : SubredditView {
        override fun setRefreshing(refreshing: Boolean) {}
        override fun showLinks(links: List<Link>) {}
        override fun onError(message: String) {}
    }
}