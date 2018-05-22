package com.rebbit.app.ui.subreddit

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rebbit.app.R
import com.rebbit.app.databinding.ItemPostBinding
import com.rebbit.data.model.Link

class SubredditAdapter(context: Context, private val isMultiReddit: Boolean = false) : RecyclerView.Adapter<SubredditAdapter.SubredditViewHolder>() {

    private val links = arrayListOf<Link>()

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditViewHolder {
        return SubredditViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_post, parent, false), isMultiReddit)
    }

    override fun getItemCount(): Int = links.size

    override fun onBindViewHolder(holder: SubredditViewHolder, position: Int) {
        holder.bind(links[position])
    }

    fun setLinks(links: List<Link>) {
        this.links.clear()
        this.links.addAll(links)
        notifyDataSetChanged()
    }

    class SubredditViewHolder(private val binding: ItemPostBinding, showAuthor: Boolean) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = LinkAdapterViewModel(showAuthor)
        }

        fun bind(link: Link) {
            binding.viewModel!!.bind(itemView.context, link)
            binding.executePendingBindings()
        }
    }
}