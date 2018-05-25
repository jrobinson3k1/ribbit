package com.rebbit.app.ui.subreddit

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.rebbit.app.di.PerFragment
import com.rebbit.data.api.SubredditClient
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

        @BindsInstance
        fun fragment(@Named("this_fragment") fragment: Fragment): SubredditFragmentComponent.Builder

        fun build(): SubredditFragmentComponent
    }

    @Module
    class SubredditFragmentModule {

        @Provides
        @PerFragment
        fun providesViewModel(
                @Named("this_fragment") fragment: Fragment,
                subredditClient: SubredditClient,
                @Named("subreddit") subreddit: String,
                @Named("io_scheduler") ioScheduler: Scheduler,
                @Named("ui_scheduler") uiScheduler: Scheduler): SubredditViewModel {
            return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return SubredditViewModel(subredditClient, subreddit, ioScheduler, uiScheduler) as T
                }

            }).get(SubredditViewModel::class.java)
        }
    }
}