package com.rebbit.data.api

import com.rebbit.data.model.Listing
import com.rebbit.data.model.Thing
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ListingApi {

    @GET("r/{subreddit}/hot?g=GLOBAL")
    fun getHot(
            @Path("subreddit") subreddit: String,
            @Query("after") after: String? = null,
            @Query("before") before: String? = null,
            @Query("count") count: Int = 0,
            @Query("limit") limit: Int = 25,
            @Query("show") show: String? = null
    ): Single<Thing<Listing>>
}