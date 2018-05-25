package com.rebbit.app.ui.subreddit

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rebbit.app.R
import com.rebbit.app.databinding.FragmentSubredditBinding
import com.rebbit.app.di.Injector
import com.rebbit.data.model.NetworkState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class SubredditFragment : Fragment() {

    @Inject
    lateinit var viewModel: SubredditViewModel

    private lateinit var binding: FragmentSubredditBinding

    private val compositeDisposable = CompositeDisposable()

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
        SubredditAdapter({ viewModel.retry() }).apply {
            binding.recyclerView.adapter = this
            viewModel.posts.safeSubscribe { submitList(it) }
            viewModel.networkState.safeSubscribe { setNetworkState(it) }
        }

        viewModel.refreshState.safeSubscribe { binding.swipeRefreshLayout.isRefreshing = it == NetworkState.LOADING }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    fun <T> Observable<T>.safeSubscribe(onNext: (T) -> Unit) {
        subscribe(onNext).addTo(compositeDisposable)
    }

    companion object {
        private const val ARG_SUBREDDIT = "subreddit"

        fun newInstance(subreddit: String) = SubredditFragment().apply {
            arguments = Bundle().apply { putString(ARG_SUBREDDIT, subreddit) }
        }
    }
}