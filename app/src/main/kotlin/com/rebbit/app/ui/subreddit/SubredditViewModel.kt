package com.rebbit.app.ui.subreddit

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.rebbit.data.api.ListingClient
import com.rebbit.data.model.Link
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.atomic.AtomicInteger

class SubredditViewModel(private val listingClient: ListingClient, private val subreddit: String, private val ioScheduler: Scheduler, private val uiScheduler: Scheduler) : ViewModel() {

    private var links: MutableLiveData<List<Link>>? = null

    private val page = AtomicInteger()
    private val disposables = CompositeDisposable()

    fun getLinks(): LiveData<List<Link>> {
        if (links == null) {
            links = MutableLiveData()
            getNextPage()
        }

        return links!!
    }

    fun getNextPage() {
        loadLinks(page.getAndIncrement())
    }

    override fun onCleared() {
        disposables.clear()
    }

    private fun loadLinks(page: Int) {
        listingClient.getSubreddit(subreddit)
                .map { it.data.children.map { it.data } }
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(
                        { links -> this.links!!.postValue(links) },
                        { error -> /* Do Something! */ }
                )
                .addTo(disposables)
    }
}