package com.rebbit.app.util

import android.databinding.BindingAdapter
import android.net.Uri
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ImageView
import com.rebbit.app.R
import com.rebbit.app.util.glide.GlideApp
import com.rebbit.data.model.Post

@BindingAdapter("thumbnailFromPost")
fun ImageView.setThumbnailFromPost(post: Post?) {
    if (post == null) {
        setImageDrawable(null)
        return
    }

    GlideApp.with(this)
            .load(
                    if (post.isSelf) R.drawable.ic_text
                    else if (!post.thumbnail.isSupportedImageUrl()) R.drawable.ic_link
                    else post.thumbnail
            )
            .circleOutline(
                    context.getColor(
                            if (post.postHint == null) android.R.color.transparent
                            else when (post.postHint) {
                                Post.Hint.Image -> R.color.dark_pastel_blue
                                Post.Hint.Video -> R.color.pastel_pink
                                else -> android.R.color.black
                            }
                    )
            )
            .into(this)
}

@BindingAdapter("url")
fun ImageView.setUrl(url: String?) {
    if (url == null || (!url.isSupportedImageUrl() && !url.isGif())) {
        setImageDrawable(null)
        return
    }

    GlideApp.with(this)
            .asDrawable()
            .load(url)
            .into(this)
}

@BindingAdapter("refreshing")
fun setRefreshing(swipeRefreshLayout: SwipeRefreshLayout, refreshing: Boolean) {
    swipeRefreshLayout.isRefreshing = refreshing
}

@BindingAdapter("onRefresh")
fun setOnRefresh(swipeRefreshLayout: SwipeRefreshLayout, onRefresh: Runnable) {
    swipeRefreshLayout.setOnRefreshListener { onRefresh.run() }
}

@BindingAdapter("onTouch")
fun setOnTouchListener(view: View, listener: View.OnTouchListener) {
    view.setOnTouchListener(listener)
}