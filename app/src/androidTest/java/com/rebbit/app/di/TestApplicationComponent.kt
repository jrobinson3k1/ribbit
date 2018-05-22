package com.rebbit.app.di

import android.content.Context
import com.rebbit.app.di.components.ApplicationComponent
import com.rebbit.app.ui.subreddit.SubredditFragmentTestComponent
import com.rebbit.app.ui.subreddit.SubredditPresenter
import dagger.Component

@AppScope
@Component(modules = [TestModule::class])
interface TestApplicationComponent : ApplicationComponent {

//    fun subredditPresenter(): SubredditPresenter

    fun context(): Context

    override fun subredditFragmentBuilder(): SubredditFragmentTestComponent.Builder
}