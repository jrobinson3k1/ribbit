package com.rebbit.app.di

import com.rebbit.app.MyApplication

class Injector private constructor() {
    companion object {
        fun get() = MyApplication.APP_GRAPH
    }
}