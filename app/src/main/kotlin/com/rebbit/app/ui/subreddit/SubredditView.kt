package com.rebbit.app.ui.subreddit

import com.rebbit.data.model.Link

interface SubredditView {

    fun setRefreshing(refreshing: Boolean)

    fun showLinks(links: List<Link>)

    fun onError(message: String)
}