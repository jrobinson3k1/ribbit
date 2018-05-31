package com.rebbit.app.ui.media

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.rebbit.app.di.PerFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [MediaViewerActivityComponent.MediaViewerActivityModule::class])
@PerFragment
interface MediaViewerActivityComponent {

    fun inject(target: MediaViewerActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun mediaUrl(@Named("media_url") mediaUrl: String): MediaViewerActivityComponent.Builder

        @BindsInstance
        fun activity(@Named("this_activity") activity: FragmentActivity): MediaViewerActivityComponent.Builder

        fun build(): MediaViewerActivityComponent
    }

    @Module
    class MediaViewerActivityModule {

        @Provides
        @PerFragment
        fun providesViewModel(@Named("this_activity") activity: FragmentActivity,
                              @Named("media_url") mediaUrl: String): MediaViewerViewModel {
            return ViewModelProviders.of(activity, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MediaViewerViewModel(mediaUrl) as T
                }

            }).get(MediaViewerViewModel::class.java)
        }
    }
}