package com.rebbit.app.ui.subreddit

import android.content.Context
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.databinding.ObservableLong
import android.view.View
import com.rebbit.app.R
import com.rebbit.app.util.TimeUtil
import com.rebbit.data.model.Post
import java.text.NumberFormat

fun Post.toPostView() = PostViewModel.PostView(title, createdUtc, author, numComments, subredditNamePrefixed, thumbnail, score, postHint)

class PostViewModel(private val context: Context, showSubreddit: Boolean) {

    val subredditVisibility = if (showSubreddit) View.VISIBLE else View.GONE
    val title: ObservableField<String> = ObservableField()

    val author: ObservableField<String> = ObservableField()
    val createdUtc: ObservableLong = ObservableLong()
    val timeAndAuthor: ObservableField<String> = object : ObservableField<String>(createdUtc, author) {
        override fun get(): String? = context.getString(R.string.time_and_author, TimeUtil.toElapsedString(context, createdUtc.get()), author.get())
    }

    val subreddit: ObservableField<String> = ObservableField()

    val commentCount: ObservableInt = ObservableInt()
    val commentText: ObservableField<String> = object : ObservableField<String>(commentCount) {
        override fun get(): String? = context.resources.getQuantityString(R.plurals.comments, commentCount.get(), commentCount.get())
    }

    val thumbnailUrl: ObservableField<String> = ObservableField()
    val hint: ObservableField<Post.Hint> = ObservableField()

    val score: ObservableInt = ObservableInt()
    val scoreText: ObservableField<String> = object : ObservableField<String>(score) {
        override fun get(): String? = NumberFormat.getNumberInstance().format(score.get())
    }

    fun bind(post: PostView) {
        title.set(post.title)
        author.set(post.author)
        createdUtc.set(post.createdUtc)
        subreddit.set(post.subreddit)
        commentCount.set(post.commentCount)
        thumbnailUrl.set(post.thumbnailUrl)
        hint.set(post.hint)
        score.set(post.score)
    }

    data class PostView(val title: String,
                        val createdUtc: Long,
                        val author: String,
                        val commentCount: Int,
                        val subreddit: String,
                        val thumbnailUrl: String,
                        val score: Int,
                        val hint: Post.Hint?)
}