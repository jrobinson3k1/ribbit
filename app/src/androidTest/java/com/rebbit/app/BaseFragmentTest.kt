package com.rebbit.app

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.v4.app.Fragment
import com.rebbit.app.di.DaggerTestApplicationComponent
import com.rebbit.app.di.TestApplicationComponent
import com.rebbit.app.ui.ShellActivity
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Rule

abstract class BaseFragmentTest<F : Fragment> {

    @Suppress("MemberVisibilityCanBePrivate")
    @get:Rule
    val activityRule = ActivityTestRule<ShellActivity>(ShellActivity::class.java, true, false)

    lateinit var graph: TestApplicationComponent
    lateinit var fragment: F
        private set

    private lateinit var lifecycleSubject: BehaviorSubject<Lifecycle.Event>

    @Before
    fun initGraph() {
        graph = DaggerTestApplicationComponent.create()
        val app = InstrumentationRegistry.getTargetContext().applicationContext as MyApplication
        app.appGraph = graph

        lifecycleSubject = BehaviorSubject.create()
    }

    fun startFragment(fragment: F): F {
        activityRule.launchActivity(null).showFragment(fragment)
        this.fragment = fragment

        fragment.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
                lifecycleSubject.onNext(event)

                if (event == Lifecycle.Event.ON_DESTROY) owner.lifecycle.removeObserver(this)
            }
        })

        waitForOnResume()

        return fragment
    }

    private fun waitForOnResume() {
        lifecycleSubject.filter { it == Lifecycle.Event.ON_RESUME }.blockingFirst()
    }
}