package com.rebbit.app.ui.subreddit

import android.view.View
import com.rebbit.data.model.Post

interface VoteEventHandler {
    fun upvote(post: Post)
    fun downvote(post: Post)
    fun removeVote(post: Post)
}

interface VoteViewEventHandler {
    fun onUpvoteClicked(v: View)
    fun onDownvoteClicked(v: View)
}