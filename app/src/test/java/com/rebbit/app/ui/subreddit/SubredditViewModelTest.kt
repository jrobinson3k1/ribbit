package com.rebbit.app.ui.subreddit

import com.nhaarman.mockito_kotlin.*
import com.rebbit.data.api.SubredditClient
import com.rebbit.data.model.Post
import com.rebbit.data.model.Listing
import com.rebbit.data.model.Thing
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SubredditViewModelTest {

    private lateinit var presenter: SubredditViewModel
    private lateinit var testScheduler: TestScheduler

    @Mock
    private lateinit var subredditClient: SubredditClient
    @Mock
    private lateinit var view: SubredditView

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        testScheduler = TestScheduler()
        presenter = SubredditPresenterImpl(subredditClient, "subreddit", testScheduler, testScheduler).apply { bind(view) }
    }

    @Test
    fun testOnRefresh() {
        val link = mock<Post>()
        val linkThing = mock<Thing<Post>> {
            on { data } doReturn link
        }
        val listing = mock<Listing> {
            on { children } doReturn listOf(linkThing)
        }
        val listingThing = mock<Thing<Listing>> {
            on { data } doReturn listing
        }

        whenever(subredditClient.getHot(any())).thenReturn(Single.just(listingThing))

        presenter.onRefresh()
        testScheduler.triggerActions()

        inOrder(subredditClient, view) {
            verify(subredditClient).getHot(eq("subreddit"))
            verify(view).setRefreshing(eq(true))
            verify(view).showLinks(eq(listOf(link)))
            verify(view).setRefreshing(eq(false))
        }

    }
}