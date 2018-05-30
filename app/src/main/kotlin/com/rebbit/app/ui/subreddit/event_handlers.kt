package com.rebbit.app.ui.subreddit

import android.view.View
import com.rebbit.data.model.Post

interface PostEventHandler {
    fun upvote(post: Post)
    fun downvote(post: Post)
    fun removeVote(post: Post)
}

interface VoteViewEventHandler {
    fun onUpvoteClicked(v: View)
    fun onDownvoteClicked(v: View)
}

interface PostViewEventHandler : VoteViewEventHandler {
    fun onPostClicked(v: View)
    fun onThumbnailClicked(v: View)
}