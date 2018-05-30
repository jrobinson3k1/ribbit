package com.rebbit.app.ui.subreddit

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.rebbit.app.R
import com.rebbit.app.databinding.ItemPostBinding
import com.rebbit.app.ui.media.MediaViewerDialogFragment
import com.rebbit.data.model.Post

class PostViewHolder(private val binding: ItemPostBinding,
                     private val eventHandler: PostEventHandler,
                     showSubreddit: Boolean) : RecyclerView.ViewHolder(binding.root), PostViewEventHandler {

    private val viewModel = PostViewModel(binding.root.context, showSubreddit)
    private lateinit var post: Post

    init {
        binding.viewModel = viewModel
        binding.eventHandler = this
    }

    override fun onPostClicked(v: View) {
        // TODO
        Toast.makeText(binding.root.context, "onPostClicked", Toast.LENGTH_SHORT).show()
    }

    override fun onThumbnailClicked(v: View) {
        if (MediaViewerDialogFragment.isEmbeddableMedia(post.url)) {
            MediaViewerDialogFragment.newInstance(post.url).show((v.context as FragmentActivity).supportFragmentManager, null)
        } else {
            v.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(post.url)))
        }
    }

    override fun onUpvoteClicked(v: View) {
        binding.vote.apply {
            upvoteView.isSelected = !upvoteView.isSelected
            downvoteView.isSelected = false
            onVoteChanged(upvoteView.isSelected, downvoteView.isSelected)
        }
    }

    override fun onDownvoteClicked(v: View) {
        binding.vote.apply {
            downvoteView.isSelected = !downvoteView.isSelected
            upvoteView.isSelected = false
            onVoteChanged(upvoteView.isSelected, downvoteView.isSelected)
        }
    }

    private fun onVoteChanged(upvote: Boolean, downvote: Boolean) {
        updateVoteColor()
        binding.dragLayout.close(100)
        when {
            upvote -> eventHandler.upvote(post)
            downvote -> eventHandler.downvote(post)
            else -> eventHandler.removeVote(post)
        }
    }

    private fun updateVoteColor() {
        binding.vote.apply {
            val fromColor = (voteContainer.background as ColorDrawable).color
            val toColor = (
                    with(binding.root.context) {
                        when {
                            upvoteView.isSelected -> getColor(R.color.upvote)
                            downvoteView.isSelected -> getColor(R.color.downvote)
                            else -> getColor(R.color.nonvote)
                        }
                    })

            ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
                duration = 350
                interpolator = LinearInterpolator()
                addUpdateListener {
                    voteContainer.setBackgroundColor(it.animatedValue as Int)
                }
            }.start()
        }
    }

    fun bind(post: Post) {
        this.post = post

        viewModel.bind(post.toPostView())
        binding.post = post
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup, eventHandler: PostEventHandler, showSubreddit: Boolean) = PostViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_post, parent,
                false
        ), eventHandler, showSubreddit)
    }
}