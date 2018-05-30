package com.rebbit.app.di.components

import android.content.Context
import com.rebbit.app.di.AppScope
import com.rebbit.app.di.modules.ApiModule
import com.rebbit.app.di.modules.StoreModule
import com.rebbit.app.ui.media.MediaViewerFragmentComponent
import com.rebbit.app.ui.subreddit.SubredditFragmentComponent
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [
    StoreModule::class,
    ApiModule::class
])
interface ApplicationComponent {

    fun subredditFragmentBuilder(): SubredditFragmentComponent.Builder

    fun mediaViewerFragmentBuilder(): MediaViewerFragmentComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Context): ApplicationComponent.Builder

        fun build(): ApplicationComponent
    }
}