package com.rebbit.app.ui.subreddit

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rebbit.app.R
import com.rebbit.app.databinding.FragmentSubredditBinding
import com.rebbit.app.di.Injector
import com.rebbit.data.model.Link
import javax.inject.Inject

class SubredditFragment : Fragment() {

    @Inject
    lateinit var viewModel: SubredditViewModel

    private lateinit var binding: FragmentSubredditBinding
    private lateinit var adapter: SubredditAdapter

    override fun onAttach(context: Context?) {
        Injector.get().subredditFragmentBuilder()
                .fragment(this)
                .subreddit(arguments!!.getString(ARG_SUBREDDIT)!!)
                .build()
                .inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subreddit, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        with(SubredditAdapter(view.context)) {
            adapter = this
            binding.recyclerView.adapter = this
        }

        viewModel.getLinks().observe(this, Observer { links ->
            if (links != null) showLinks(links)
        })
    }

    fun setRefreshing(refreshing: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = refreshing
    }

    fun showLinks(links: List<Link>) {
        adapter.setLinks(links)
    }

    fun onError(message: String) {
        Toast.makeText(context!!, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val ARG_SUBREDDIT = "subreddit"

        fun newInstance(subreddit: String) = SubredditFragment().apply {
            arguments = Bundle().apply { putString(ARG_SUBREDDIT, subreddit) }
        }
    }
}