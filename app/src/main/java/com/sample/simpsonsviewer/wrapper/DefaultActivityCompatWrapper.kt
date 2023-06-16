package com.sample.simpsonsviewer.wrapper

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat
import com.sample.simpsonsviewer.wrapper.ActivityCompatWrapper

class DefaultActivityCompatWrapper : ActivityCompatWrapper {
    override fun shouldShowRequestPermissionRationale(
        activity: Activity,
        nextPermission: String
    ): Boolean = ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        Manifest.permission.CAMERA
    )

    override fun requestPermissions(
        activity: Activity,
        list: Array<String>,
        identifier: Int
    ) {
        ActivityCompat.requestPermissions(activity, list, identifier)
    }
}