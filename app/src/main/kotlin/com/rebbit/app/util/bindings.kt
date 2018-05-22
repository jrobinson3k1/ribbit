package com.rebbit.app.util

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v4.widget.SwipeRefreshLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.rebbit.app.R
import com.rebbit.data.model.Link


@BindingAdapter("app:thumbnailUrl", "app:postHint", requireAll = true)
fun ImageView.setThumbnailUrl(thumbnailUrl: String?, postHint: Link.PostHint?) {
    var requestBuilder: RequestBuilder<Bitmap> = Glide.with(this).asBitmap()
    requestBuilder = when {
        postHint == Link.PostHint.Self -> requestBuilder.load(R.drawable.ic_text)
        thumbnailUrl == null || !thumbnailUrl.isImage() -> requestBuilder.load(R.drawable.ic_link)
        else -> requestBuilder.load(thumbnailUrl)
    }

    requestBuilder.apply(RequestOptions.centerCropTransform()).into(object : BitmapImageViewTarget(this) {
        override fun setResource(resource: Bitmap?) {
            var circularBitmapDrawable: RoundedBitmapDrawable? = null
            if (resource != null) {
                val colorResId = when (postHint) {
                    Link.PostHint.Image -> R.color.dark_pastel_blue
                    Link.PostHint.Video -> R.color.pastel_pink
                    Link.PostHint.Self -> android.R.color.transparent
                    else -> android.R.color.black
                }

                circularBitmapDrawable = RoundedBitmapDrawableFactory.create(this@setThumbnailUrl.context.resources, addBorder(resource, this@setThumbnailUrl.context, colorResId))
                circularBitmapDrawable.isCircular = true
            }
            this@setThumbnailUrl.setImageDrawable(circularBitmapDrawable)
        }
    })
}

private fun String.isImage() = matches(Regex("^.+\\.(?:jpg|png)\$"))

@BindingAdapter("app:refreshing")
fun setRefreshing(swipeRefreshLayout: SwipeRefreshLayout, refreshing: Boolean) {
    swipeRefreshLayout.isRefreshing = refreshing
}

@BindingAdapter("app:onRefresh")
fun setOnRefresh(swipeRefreshLayout: SwipeRefreshLayout, onRefresh: SwipeRefreshLayout.OnRefreshListener?) {
    swipeRefreshLayout.setOnRefreshListener(onRefresh)
}

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