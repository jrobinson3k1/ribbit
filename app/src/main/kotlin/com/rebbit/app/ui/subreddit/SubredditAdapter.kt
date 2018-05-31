package com.rebbit.app.ui.subreddit

import android.arch.paging.PagedListAdapter
import android.support.v4.app.FragmentActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.rebbit.app.R
import com.rebbit.data.model.NetworkState
import com.rebbit.data.model.Post
import com.rebbit.data.model.Status.FAILED
import com.rebbit.data.model.Status.RUNNING

class SubredditAdapter(
        private val activity: FragmentActivity,
        private val eventHandler: PostEventHandler,
        private val retryCallback: () -> Unit,
        private val isMultiReddit: Boolean = false) : PagedListAdapter<Post, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_post -> PostViewHolder.create(activity, parent, eventHandler, isMultiReddit)
            R.layout.item_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_post -> (holder as PostViewHolder).bind(getItem(position)!!)
            R.layout.item_network_state -> (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_post
        }
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    class NetworkStateViewHolder(view: View, private val retryCallback: () -> Unit) : RecyclerView.ViewHolder(view) {

        private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        private val retry = view.findViewById<Button>(R.id.retry_button)
        private val errorMsg = view.findViewById<TextView>(R.id.error_msg)

        init {
            retry.setOnClickListener {
                retryCallback()
            }
        }

        fun bind(networkState: NetworkState?) {
            progressBar.visibility = toVisibility(networkState?.status == RUNNING)
            retry.visibility = toVisibility(networkState?.status == FAILED)
            errorMsg.visibility = toVisibility(networkState?.msg != null)
            errorMsg.text = networkState?.msg
        }

        companion object {
            fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_network_state, parent, false)
                return NetworkStateViewHolder(view, retryCallback)
            }

            fun toVisibility(constraint: Boolean): Int {
                return if (constraint) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Post>() {
            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                    oldItem.toPostView() == newItem.toPostView()

            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                    oldItem.name == newItem.name
        }
    }
}