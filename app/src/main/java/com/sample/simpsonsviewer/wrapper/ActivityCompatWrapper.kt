package com.sample.simpsonsviewer.wrapper

import android.app.Activity

interface ActivityCompatWrapper {
    fun shouldShowRequestPermissionRationale(activity: Activity, nextPermission: String): Boolean
    fun requestPermissions(activity: Activity, list: Array<String>, identifier: Int)
}
