package com.rebbit.data.api

import com.nhaarman.mockito_kotlin.*
import com.rebbit.data.model.Listing
import com.rebbit.data.model.Thing
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ListingClientTest {

    private lateinit var client: ListingClient

    @Mock
    private lateinit var listingApi: ListingApi

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        client = ListingClient(listingApi)
    }

    @Test
    fun testAccessToken() {
        val listingThing = mock<Thing<Listing>>()
        whenever(listingApi.getHot("subreddit")).thenReturn(Single.just(listingThing))

        client.getSubreddit("subreddit")
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(listingThing)

        verify(listingApi).getHot(eq("subreddit"), isNull(), isNull(), eq(0), eq(25), isNull())
    }
}