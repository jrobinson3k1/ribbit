package com.rebbit.app

import android.app.Application
import com.rebbit.app.di.Injector
import com.rebbit.app.di.components.DaggerApplicationComponent
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.set(DaggerApplicationComponent.builder()
                .application(this)
                .build())

        Timber.plant(Timber.DebugTree())
    }
}