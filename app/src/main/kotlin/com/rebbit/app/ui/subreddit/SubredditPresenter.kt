package com.rebbit.app.ui.subreddit

import com.rebbit.app.mvp.Presenter
import com.rebbit.data.api.ListingClient
import com.rebbit.data.model.Link
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

interface SubredditPresenter : Presenter<SubredditView> {
    fun onRefresh()
}

class SubredditPresenterImpl(private val listingClient: ListingClient, private val subreddit: String, private val ioScheduler: Scheduler, private val uiScheduler: Scheduler) : SubredditPresenter {

    private var view: SubredditView? = null
    private val disposables = CompositeDisposable()

    override fun bind(view: SubredditView) {
        this.view = view
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
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
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