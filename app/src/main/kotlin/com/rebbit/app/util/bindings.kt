package com.rebbit.app.util

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.*
import android.support.v4.content.ContextCompat
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
                    else if (!post.thumbnail.isImage()) R.drawable.ic_link
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
    if (url == null || !url.isSupportedImageUrl()) {
        setImageDrawable(null)
        return
    }

    GlideApp.with(this).load(url).into(this)
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

private val IMAGE_REGEX = Regex("^.+\\.(?:jpg|png)\$")

private fun String.isImage() = matches(IMAGE_REGEX)

private fun addBorder(resource: Bitmap, context: Context, colorResId: Int): Bitmap {
    val w = resource.width
    val h = resource.height
    val radius = Math.min(h / 2, w / 2)
    val output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888)
    val p = Paint()
    p.isAntiAlias = true
    val c = Canvas(output)
    c.drawARGB(0, 0, 0, 0)
    p.style = Paint.Style.FILL
    c.drawCircle(w / 2 + 4f, h / 2 + 4f, radius.toFloat(), p)
    p.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    c.drawBitmap(resource, 4f, 4f, p)
    p.xfermode = null
    p.style = Paint.Style.STROKE
    p.color = ContextCompat.getColor(context, colorResId)
    p.strokeWidth = 12f
    c.drawCircle(w / 2 + 4f, h / 2 + 4f, radius.toFloat(), p)
    return output
}