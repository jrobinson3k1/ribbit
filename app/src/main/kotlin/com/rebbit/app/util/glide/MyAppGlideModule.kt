package com.rebbit.app.util.glide

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.rebbit.app.BuildConfig

@GlideModule
class MyAppGlideModule: AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setLogLevel(if(BuildConfig.DEBUG) Log.VERBOSE else Log.ERROR)
    }
}