package com.rebbit.app.di

import com.rebbit.app.di.components.ApplicationComponent

object Injector {

    private lateinit var graph: ApplicationComponent

    fun set(graph: ApplicationComponent) {
        this.graph = graph
    }

    fun get() = graph
}