package com.rebbit.app.util

import android.content.Context
import android.support.annotation.PluralsRes
import com.rebbit.app.R
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.floor

object TimeUtil {

    private const val YEAR = 365L
    private const val MONTH = 31L

    fun toElapsedString(context: Context, seconds: Long): String {
        val nowSeconds = Calendar.getInstance(TimeZone.getTimeZone("utc")).timeInMillis / 1000
        val difference = nowSeconds - seconds

        val days = TimeUnit.SECONDS.toDays(difference)
        if (days >= YEAR) return context.getTimeString(R.plurals.years, floor(days.toFloat() / YEAR).toInt())
        if (days >= MONTH) return context.getTimeString(R.plurals.months, floor(days.toFloat() / MONTH).toInt())
        if (days >= 1) return context.getTimeString(R.plurals.days, days.toInt())

        val hours = TimeUnit.SECONDS.toHours(difference)
        if (hours >= 1) return context.getTimeString(R.plurals.hours, hours.toInt())

        val minutes = TimeUnit.SECONDS.toMinutes(difference)
        if (minutes >= 1) return context.getTimeString(R.plurals.minutes, minutes.toInt())

        return context.getTimeString(R.plurals.seconds, difference.toInt())
    }

    private fun Context.getTimeString(@PluralsRes resId: Int, time: Int) = resources.getQuantityString(resId, time, time)
}