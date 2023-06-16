package com.sample.simpsonsviewer.api.network

import android.content.Context

interface AppNetworkManager {
    fun isNetworkAvailable(
        context: Context,
        networkCapability: Int? = null,
    ): Boolean
    fun networkSpeed(context: Context?)
    fun isConnected(context: Context?): Boolean
}
