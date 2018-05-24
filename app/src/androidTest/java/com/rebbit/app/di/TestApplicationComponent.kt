package com.rebbit.app.di

import com.rebbit.app.di.components.ApplicationComponent
import com.rebbit.app.ui.subreddit.SubredditFragmentTestComponent
import com.rebbit.app.ui.subreddit.SubredditViewModel
import dagger.Component

@AppScope
@Component(modules = [TestModule::class])
interface TestApplicationComponent : ApplicationComponent {

    fun subredditPresenter(): SubredditViewModel

    override fun subredditFragmentBuilder(): SubredditFragmentTestComponent.Builder
}