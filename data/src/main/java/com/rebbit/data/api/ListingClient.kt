package com.rebbit.data.api

import com.rebbit.data.model.Listing
import com.rebbit.data.model.Thing
import io.reactivex.Single

class ListingClient(private val api: ListingApi) {

    fun getSubreddit(subreddit: String): Single<Thing<Listing>> = api.getHot(subreddit)
}