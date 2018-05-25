package com.rebbit.data.api

import com.nhaarman.mockito_kotlin.*
import com.rebbit.data.model.Listing
import com.rebbit.data.model.Thing
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SubredditClientTest {

    private lateinit var client: SubredditClient

    @Mock
    private lateinit var subredditApi: SubredditApi

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        client = SubredditClient(subredditApi)
    }

    @Test
    fun testAccessToken() {
        val listingThing = mock<Thing<Listing>>()
        whenever(subredditApi.getHot("subreddit")).thenReturn(Single.just(listingThing))

        client.getHot("subreddit")
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(listingThing)

        verify(subredditApi).getHot(eq("subreddit"), isNull(), isNull(), eq(0), eq(25), isNull())
    }
}