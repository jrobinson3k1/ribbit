package com.rebbit.app

import android.app.Application
import com.rebbit.app.di.Injector
import com.rebbit.app.di.components.ApplicationComponent
import com.rebbit.app.di.components.DaggerApplicationComponent
import timber.log.Timber

class MyApplication : Application() {

    var appGraph: ApplicationComponent? = null
        set(value) {
            Injector.set(value!!)
        }

    override fun onCreate() {
        super.onCreate()
        appGraph = DaggerApplicationComponent.builder()
                .application(this)
                .build()

        Timber.plant(Timber.DebugTree())
    }
}