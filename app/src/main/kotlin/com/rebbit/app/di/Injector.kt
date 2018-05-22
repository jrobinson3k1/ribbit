package com.rebbit.app.di

import com.rebbit.app.di.components.ApplicationComponent

class Injector private constructor() {
    companion object {

        private lateinit var graph: ApplicationComponent

        fun set(graph: ApplicationComponent) {
            this.graph = graph
        }

        fun get() = graph
    }
}