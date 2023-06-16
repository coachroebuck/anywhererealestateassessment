package com.sample.simpsonsviewer.wrapper

import android.content.Context

interface ContextCompatWrapper {
    fun checkSelfPermission(
        context: Context,
        nextPermission: String
    ): Int
}
