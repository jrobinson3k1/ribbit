package com.rebbit.app.di.modules

import com.rebbit.app.di.AppScope
import com.rebbit.data.api.SubredditApi
import com.rebbit.data.api.SubredditClient
import com.rebbit.data.model.Listing
import com.rebbit.data.model.Thing
import com.rebbit.testutil.LoremCreator
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class ApiModule {

    @Provides
    @Named("io_scheduler")
    fun providesIoScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Named("ui_scheduler")
    fun providesUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @AppScope
    fun providesListingApi(): SubredditApi = object : SubredditApi {
        override fun getHot(subreddit: String, after: String?, before: String?, count: Int, limit: Int, show: String?): Single<Thing<Listing>> {
            val posts = LoremCreator.posts(limit)
            val thing = Thing("1", "all", "kind", Listing("modhash", 1, posts.map { Thing("id", "name", "kind", it) }, null, null))
            return Single.just(thing).delay(0, TimeUnit.SECONDS)
        }
    }

    @Provides
    @AppScope
    fun providesListingClient(api: SubredditApi): SubredditClient = SubredditClient(api)
}
