package com.rebbit.app

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.v4.app.Fragment
import com.rebbit.app.di.DaggerTestApplicationComponent
import com.rebbit.app.di.TestApplicationComponent
import com.rebbit.app.ui.ShellActivity
import org.junit.Before
import org.junit.Rule

abstract class BaseFragmentTest<F : Fragment> {

    @Suppress("MemberVisibilityCanBePrivate")
    @get:Rule
    val activityRule = ActivityTestRule<ShellActivity>(ShellActivity::class.java, true, false)
    @get:Rule
    val lifecycleRule = LifecycleRule()

    lateinit var graph: TestApplicationComponent
    lateinit var fragment: F
        private set

    @Before
    fun initGraph() {
        graph = DaggerTestApplicationComponent.create()
        val app = InstrumentationRegistry.getTargetContext().applicationContext as MyApplication
        app.appGraph = graph
    }

    fun startFragment(fragment: F): F {
        this.fragment = fragment
        lifecycleRule.bind(fragment)
        activityRule.launchActivity(null).showFragment(fragment)
        lifecycleRule.waitForQueuedEvent()

        return fragment
    }
}