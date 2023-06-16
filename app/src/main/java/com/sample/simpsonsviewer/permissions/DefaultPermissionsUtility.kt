package com.sample.simpsonsviewer.permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import com.sample.simpsonsviewer.log.DefaultLogAdapter
import com.sample.simpsonsviewer.log.LogAdapter
import com.sample.simpsonsviewer.wrapper.ActivityCompatWrapper
import com.sample.simpsonsviewer.wrapper.ContextCompatWrapper
import com.sample.simpsonsviewer.wrapper.DefaultActivityCompatWrapper
import com.sample.simpsonsviewer.wrapper.DefaultContextCompatWrapper

class DefaultPermissionsUtility(
    private val logAdapter: LogAdapter = DefaultLogAdapter(),
    private val requestPermissionLauncher: ActivityResultLauncher<String>,
    private val contextCompatWrapper: ContextCompatWrapper = DefaultContextCompatWrapper(),
    private val activityCompatWrapper: ActivityCompatWrapper = DefaultActivityCompatWrapper(),
    private val onRequestPermissionsCompleted: () -> Unit,
    private val onPermissionExplanationRequired: () -> Unit,
) : PermissionsUtility {

    // region Properties

    private lateinit var permissions: List<String>

    // endregion

    // region Override Methods

    override fun requestPermissions(activity: Activity, permissions: List<String>) {
        this.permissions = permissions
        continueRequestingPermissions(activity)
    }

    // endregion

    // region Private Methods

    override fun shouldRequestPermissions() = permissions.isNotEmpty()

    override fun continueRequestingPermissions(activity: Activity) {
        if(permissions.isNotEmpty()) {
            val mutableList = permissions.toMutableList()
            val nextPermission = mutableList.removeFirst()

            permissions = mutableList.toList()
            requestNextPermission(activity, nextPermission)
        } else {
            onRequestPermissionsCompleted.invoke()
        }
    }

    private fun requestNextPermission(activity: Activity, nextPermission: String) {
        if( contextCompatWrapper.checkSelfPermission(
                activity,
                nextPermission
            ) != PackageManager.PERMISSION_GRANTED) {
            if(activityCompatWrapper.shouldShowRequestPermissionRationale(
                    activity,
                    nextPermission
                )) {
                // Show an explanation to the user *asynchronously* -- don't block.
                // This thread waiting for the user's response! After the user sees the explanation,
                // try again to request the permission
                val mutableList = permissions.toMutableList()
                mutableList.add(0, nextPermission)
                permissions = mutableList.toList()
                onPermissionExplanationRequired()
            } else {
                requestPermissionLauncher.launch(nextPermission)
            }
        } else {
            logAdapter.i("Permission granted=[$nextPermission]")
            continueRequestingPermissions(activity)
        }
    }

    // endregion
}
