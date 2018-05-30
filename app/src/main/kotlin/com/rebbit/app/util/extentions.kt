package com.rebbit.app.util

import android.util.Patterns
import java.util.regex.Pattern

private val IMAGE = Pattern.compile("(.*/)*.+\\.(png|jpg|bmp|jpeg|webp|PNG|JPG|BMP|JPEG|WEBP)$")
private val GIF = Pattern.compile("(.*/)*.+\\.(gif|gifv|GIF|GIFV)$")
private val VIDEO = Pattern.compile("(.*/)*.+\\.(3gp|mp4|webm|3GP|MP4|WEBM)$")

fun CharSequence.isUrl() = Patterns.WEB_URL.matcher(this).matches()

fun CharSequence.isSupportedImageUrl() = isUrl() && IMAGE.matcher(this).matches()

fun CharSequence.isGif() = isUrl() && GIF.matcher(this).matches()

fun CharSequence.isSupportedVideoUrl() = isUrl() && VIDEO.matcher(this).matches()