package com.rebbit.app.ui

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

class ShellActivity : AppCompatActivity() {

    fun showFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit()
    }
}