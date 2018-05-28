package com.rebbit.app.di.modules

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.manager.RequestManagerRetriever
import com.rebbit.app.di.AppScope
import dagger.Module
import dagger.Provides

@Module
class MediaModule {

    @Provides
    @AppScope
    fun providesRequestManagerRetriever(context: Context): RequestManagerRetriever {
        return Glide.get(context).requestManagerRetriever
    }
}