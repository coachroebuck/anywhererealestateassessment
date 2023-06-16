package com.sample.simpsonsviewer.wrapper

import android.content.Context
import android.content.Intent
import android.os.Bundle

interface LaunchActivityWrapper {
    fun <T> navigate(
        applicationContext: Context,
        clazz: Class<T>,
        flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK,
        bundle: Bundle? = null,
        vararg extras: Any?
    )
}
