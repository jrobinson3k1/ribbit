package com.rebbit.app.ui.media

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.rebbit.app.di.PerFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [MediaViewerFragmentComponent.MediaViewerFragmentModule::class])
@PerFragment
interface MediaViewerFragmentComponent {

    fun inject(target: MediaViewerDialogFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun mediaUrl(@Named("mediaUrl") mediaUrl: String): MediaViewerFragmentComponent.Builder

        @BindsInstance
        fun fragment(@Named("this_fragment") fragment: Fragment): MediaViewerFragmentComponent.Builder

        fun build(): MediaViewerFragmentComponent
    }

    @Module
    class MediaViewerFragmentModule {

        @Provides
        @PerFragment
        fun providesViewModel(@Named("this_fragment") fragment: Fragment,
                              @Named("mediaUrl") mediaUrl: String): MediaViewerViewModel {
            return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MediaViewerViewModel(mediaUrl) as T
                }

            }).get(MediaViewerViewModel::class.java)
        }
    }
}