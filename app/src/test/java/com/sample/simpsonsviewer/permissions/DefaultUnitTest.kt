package com.sample.simpsonsviewer.permissions

import android.app.Activity
import io.mockk.mockk

open class DefaultUnitTest {

    protected lateinit var activity: Activity

    open fun setUp() {
        activity = mockk(relaxed = true)
    }

    open fun tearDown() { }
}
