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

    @get:Rule
    val activityRule = ActivityTestRule<ShellActivity>(ShellActivity::class.java, true, false)

    lateinit var fragment: F
        private set

    lateinit var graph: TestApplicationComponent

    @Before
    fun initGraph() {
        graph = DaggerTestApplicationComponent.create()
        val app = InstrumentationRegistry.getTargetContext().applicationContext as MyApplication
        app.appGraph = graph
    }

    fun startFragment(fragment: F): F {
        activityRule.launchActivity(null).showFragment(fragment)
        this.fragment = fragment
        return fragment
    }
}