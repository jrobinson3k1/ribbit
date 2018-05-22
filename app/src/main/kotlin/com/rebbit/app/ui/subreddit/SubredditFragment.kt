package com.rebbit.app.ui.subreddit

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

class SubredditFragment : Fragment(), SubredditView {

    @Inject
    lateinit var presenter: SubredditPresenter

    private lateinit var binding: FragmentSubredditBinding
    private lateinit var adapter: SubredditAdapter

    override fun onAttach(context: Context?) {
        Injector.get().subredditFragmentBuilder()
                .subreddit(arguments!!.getString(ARG_SUBREDDIT)!!)
                .build()
                .inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentSubredditBinding>(inflater, R.layout.fragment_subreddit, container, false).apply {
            presenter = this@SubredditFragment.presenter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        with(SubredditAdapter(view.context)) {
            adapter = this
            binding.recyclerView.adapter = this
        }

        presenter.bind(this)
    }

    override fun onDestroyView() {
        presenter.unBind()
        super.onDestroyView()
    }

    override fun setRefreshing(refreshing: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = refreshing
    }

    override fun showLinks(links: List<Link>) {
        adapter.setLinks(links)
    }

    override fun onError(message: String) {
        Toast.makeText(context!!, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val ARG_SUBREDDIT = "subreddit"

        fun newInstance(subreddit: String) = SubredditFragment().apply {
            arguments = Bundle().apply { putString(ARG_SUBREDDIT, subreddit) }
        }
    }
}