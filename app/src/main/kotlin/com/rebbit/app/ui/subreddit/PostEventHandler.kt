package com.rebbit.app.ui.subreddit

import com.rebbit.data.model.Post

interface PostEventHandler {

    fun upvote(post: Post)
    fun downvote(post: Post)
}