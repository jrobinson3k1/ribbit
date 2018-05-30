package com.rebbit.app.util.glide

import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.RequestOptions


@GlideExtension
object MyAppGlideExtensions {

    @GlideOption
    @JvmStatic
    fun circleOutline(options: RequestOptions, color: Int) {
        options.transform(CircleOutlineTransformation(color))
    }
}