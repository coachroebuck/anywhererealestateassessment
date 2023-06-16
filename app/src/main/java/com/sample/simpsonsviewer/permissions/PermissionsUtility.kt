package com.sample.simpsonsviewer.permissions

import android.app.Activity

interface PermissionsUtility {
    fun requestPermissions(activity: Activity, permissions: List<String>)
    fun continueRequestingPermissions(activity: Activity)
    fun shouldRequestPermissions(): Boolean
}