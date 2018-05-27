package com.rebbit.app.ui.subreddit

import android.content.Context
import android.databinding.ObservableField
import com.rebbit.app.R
import com.rebbit.app.util.TimeUtil
import com.rebbit.data.model.Post

fun Post.toPostView() = PostViewModel.PostView(title, createdUtc, author, subreddit, thumbnail, postHint)

class PostViewModel(private val showAuthor: Boolean) {

    val title: ObservableField<String> = ObservableField()
    val info: ObservableField<String> = ObservableField()
    val thumbnailUrl: ObservableField<String> = ObservableField()
    val hint: ObservableField<Post.Hint> = ObservableField()

    fun bind(context: Context, post: Post?) {
        title.set(post?.title)
        info.set(
                post?.let {
                    if (showAuthor) context.getString(R.string.link_info, TimeUtil.toTimeDisplay(context, post.createdUtc), post.subreddit)
                    else context.getString(R.string.link_info_with_author, TimeUtil.toTimeDisplay(context, post.createdUtc), post.author, post.subreddit)
                }
        )
        thumbnailUrl.set(post?.thumbnail)
        hint.set(post?.postHint)
    }

    data class PostView(val title: String,
                        val createdUtc: Long,
                        val author: String,
                        val subreddit: String,
                        val thumbnailUrl: String,
                        val hint: Post.Hint?)
}