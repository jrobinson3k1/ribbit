package com.rebbit.app.di.components

import android.content.Context
import com.bumptech.glide.manager.RequestManagerRetriever
import com.rebbit.app.di.AppScope
import com.rebbit.app.di.modules.ApiModule
import com.rebbit.app.di.modules.MediaModule
import com.rebbit.app.di.modules.StoreModule
import com.rebbit.app.ui.subreddit.SubredditFragmentComponent
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [
    StoreModule::class,
    ApiModule::class,
    MediaModule::class
])
interface ApplicationComponent {

    fun requestManagerRetriever(): RequestManagerRetriever

    fun subredditFragmentBuilder(): SubredditFragmentComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Context): ApplicationComponent.Builder

        fun build(): ApplicationComponent
    }
}