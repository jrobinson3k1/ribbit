package com.rebbit.app.ui.subreddit

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import com.github.tmurakami.dexopener.DexOpener
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.rebbit.app.BaseFragmentTest
import com.rebbit.app.ModelCreator
import com.rebbit.app.R
import com.rebbit.data.model.Link
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SubredditFragmentTest : BaseFragmentTest<SubredditFragment>() {

    private lateinit var presenter: SubredditPresenter
    private lateinit var view: SubredditView

    @Before
    fun init() {
        presenter = graph.subredditPresenter()
        with(SubredditFragment.newInstance("subreddit")) {
            this@SubredditFragmentTest.view = this
            startFragment(this)
        }
    }

    @Test
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun testInitialState() {
        val link = ModelCreator.link(
                title = "Title",
                author = "Author",
                subreddit = "subreddit",
                createdUtc = 10000000,
                postHint = Link.PostHint.Image
        )

        whenever(presenter.onRefresh()).thenAnswer {
            view.showLinks(listOf(link))
        }

        lifecycleRule.waitFor(Lifecycle.Event.ON_RESUME)

        inOrder(presenter) {
            verify(presenter).bind(fragment)
            verify(presenter).onRefresh()
        }

        onView(withId(R.id.titleTextView)).check(matches(withText("Title")))

        // TODO
//        onView(withId(R.id.infoTextView)).check(matches(withText("")))
    }
}