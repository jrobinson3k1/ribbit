package com.rebbit.app.ui.subreddit

import com.rebbit.app.di.PerFragment
import com.rebbit.data.api.ListingClient
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.Scheduler
import javax.inject.Named

@Subcomponent(modules = [SubredditFragmentComponent.SubredditFragmentModule::class])
@PerFragment
interface SubredditFragmentComponent {

    fun inject(target: SubredditFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun subreddit(@Named("subreddit") subreddit: String): SubredditFragmentComponent.Builder

        fun build(): SubredditFragmentComponent
    }

    @Module
    class SubredditFragmentModule {

        @Provides
        @PerFragment
        fun providesPresenter(
                listingClient: ListingClient,
                @Named("subreddit") subreddit: String,
                @Named("io_scheduler") ioScheduler: Scheduler,
                @Named("ui_scheduler") uiScheduler: Scheduler): SubredditPresenter {
            return SubredditPresenterImpl(listingClient, subreddit, ioScheduler, uiScheduler)
        }
    }
}