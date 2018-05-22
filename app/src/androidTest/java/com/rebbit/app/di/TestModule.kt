package com.rebbit.app.di

import android.content.Context
import android.test.mock.MockContext
import com.nhaarman.mockito_kotlin.mock
import com.rebbit.app.store.UserStore
import com.rebbit.app.ui.subreddit.SubredditPresenter
import com.rebbit.data.api.AuthClient
import com.rebbit.data.api.ListingClient
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import javax.inject.Named

@Module
class TestModule {

    @Provides
    @Named("io_scheduler")
    fun providesIoScheduler(): Scheduler = TestScheduler()

    @Provides
    @Named("ui_scheduler")
    fun providesUiScheduler(): Scheduler = TestScheduler()

    @Provides
    fun providesContext(): Context = mock()

    @Provides
    fun providesUserStore():UserStore = mock()

    @Provides
    fun providesAuthClient():AuthClient = mock()

    @Provides
    fun providesListingClient(): ListingClient = mock()

    @Provides
    fun providesSubredditPresenter(): SubredditPresenter = mock()
}