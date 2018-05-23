package com.rebbit.app

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Lifecycle.Event.*
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import io.reactivex.subjects.ReplaySubject
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class LifecycleRule(private val defaultEvent: Lifecycle.Event = ON_RESUME) : TestWatcher() {

    private var lifecycleSubject: ReplaySubject<Lifecycle.Event>? = null
    private var queuedEvent: Lifecycle.Event? = null

    override fun starting(description: Description) {
        if (description.isTest) {
            queuedEvent = description.getAnnotation(OnLifecycleEvent::class.java)?.value ?: defaultEvent
        }
    }

    override fun finished(description: Description?) {
        lifecycleSubject?.onComplete()
        lifecycleSubject = null
        queuedEvent = null
    }

    fun waitFor(event: Lifecycle.Event) {
        if (listOf(ON_CREATE, ON_START, ON_RESUME).none { it == event }) throw IllegalArgumentException("Unsupported event: ${event.name}")
        lifecycleSubject!!.filter { it == event }.blockingFirst()
    }

    fun waitForQueuedEvent() {
        waitFor(queuedEvent!!)
    }

    fun bind(activity: AppCompatActivity) {
        bind(activity.lifecycle)
    }

    fun bind(fragment: Fragment) {
        bind(fragment.lifecycle)
    }

    fun bind(lifecycle: Lifecycle) {
        lifecycleSubject = ReplaySubject.create()
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
                lifecycleSubject?.onNext(event)

                if (event == Lifecycle.Event.ON_DESTROY) owner.lifecycle.removeObserver(this)
            }
        })
    }
}