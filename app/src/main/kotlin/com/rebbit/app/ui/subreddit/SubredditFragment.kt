package com.rebbit.app.ui.subreddit

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rebbit.app.R
import com.rebbit.app.databinding.FragmentSubredditBinding
import com.rebbit.app.di.Injector
import com.rebbit.app.ui.base.BaseFragment
import com.rebbit.data.model.NetworkState
import javax.inject.Inject

class SubredditFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: SubredditViewModel

    private lateinit var binding: FragmentSubredditBinding

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
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        SubredditAdapter(viewModel, { viewModel.retry() }, viewModel.isMultiReddit()).apply {
            binding.recyclerView.adapter = this
            viewModel.posts.safeSubscribe { submitList(it) }
            viewModel.networkState.safeSubscribe { setNetworkState(it) }
        }

        viewModel.refreshState.safeSubscribe { if (it == NetworkState.LOADED) binding.swipeRefreshLayout.isRefreshing = false }
    }

    companion object {
        private const val ARG_SUBREDDIT = "subreddit"

        fun newInstance(subreddit: String) = SubredditFragment().apply {
            arguments = Bundle().apply { putString(ARG_SUBREDDIT, subreddit) }
        }
    }
}