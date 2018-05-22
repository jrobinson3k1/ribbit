package com.rebbit.app.ui.subreddit

import com.rebbit.app.di.PerFragment
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent
@PerFragment
interface SubredditFragmentTestComponent : SubredditFragmentComponent {

    @Subcomponent.Builder
    interface Builder : SubredditFragmentComponent.Builder {
        @BindsInstance
        override fun subreddit(@Named("subreddit") subreddit: String): SubredditFragmentTestComponent.Builder

        override fun build(): SubredditFragmentTestComponent
    }
}