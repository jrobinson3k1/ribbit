package com.rebbit.app.ui.subreddit

import android.content.Context
import android.databinding.ObservableField
import com.rebbit.app.R
import com.rebbit.app.util.TimeUtil
import com.rebbit.data.model.Link

class LinkAdapterViewModel(private val showAuthor: Boolean) {

    val title: ObservableField<String> = ObservableField()
    val info: ObservableField<String> = ObservableField()
    val thumbnailUrl: ObservableField<String> = ObservableField()
    val postHint: ObservableField<Link.PostHint> = ObservableField()

    fun bind(context: Context, link: Link) {
        title.set(link.title)
        info.set(
                if(showAuthor) context.getString(R.string.link_info, TimeUtil.toTimeDisplay(context, link.createdUtc), link.subreddit)
                else context.getString(R.string.link_info_with_author, TimeUtil.toTimeDisplay(context, link.createdUtc), link.author, link.subreddit)
        )
        thumbnailUrl.set(link.thumbnail)
        postHint.set(link.postHint)
    }
}