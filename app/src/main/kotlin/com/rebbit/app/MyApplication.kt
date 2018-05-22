package com.rebbit.app

import android.app.Application
import com.rebbit.app.di.components.ApplicationComponent
import com.rebbit.app.di.components.DaggerApplicationComponent
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        APP_GRAPH = DaggerApplicationComponent.builder()
                .application(this)
                .build()

        Timber.plant(Timber.DebugTree())
    }

    companion object {
        lateinit var APP_GRAPH: ApplicationComponent
            private set
    }
}