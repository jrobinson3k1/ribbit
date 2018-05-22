package com.rebbit.app.ui.subreddit

import android.support.test.runner.AndroidJUnit4
import com.nhaarman.mockito_kotlin.verify
import com.rebbit.app.BaseFragmentTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SubredditFragmentTest : BaseFragmentTest<SubredditFragment>() {

    @Before
    fun init() {
        startFragment(SubredditFragment.newInstance("subreddit"))
    }

    @Test
    fun testInitialState() {
        verify(fragment.presenter).onRefresh()
    }
}