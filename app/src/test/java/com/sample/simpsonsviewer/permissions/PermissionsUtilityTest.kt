package com.sample.simpsonsviewer.permissions

import androidx.activity.result.ActivityResultLauncher
import com.sample.simpsonsviewer.log.LogAdapter
import com.sample.simpsonsviewer.wrapper.ActivityCompatWrapper
import com.sample.simpsonsviewer.wrapper.ContextCompatWrapper
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class PermissionsUtilityTest : DefaultUnitTest() {

    private lateinit var logAdapter: LogAdapter
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var contextCompatWrapper: ContextCompatWrapper
    private lateinit var activityCompatWrapper: ActivityCompatWrapper
    private lateinit var onRequestPermissionsCompleted: () -> Unit
    private lateinit var onPermissionExplanationRequired: () -> Unit
    private lateinit var permissionsUtility: PermissionsUtility

    @Before
    override fun setUp() {
        super.setUp()
        logAdapter = mockk(relaxed = true)
        requestPermissionLauncher = mockk(relaxed = true)
        contextCompatWrapper = mockk(relaxed = true)
        activityCompatWrapper = mockk(relaxed = true)
        onRequestPermissionsCompleted = mockk(relaxed = true)
        onPermissionExplanationRequired = mockk(relaxed = true)
        permissionsUtility = DefaultPermissionsUtility(
            logAdapter = logAdapter,
            requestPermissionLauncher = requestPermissionLauncher,
            contextCompatWrapper = contextCompatWrapper,
            activityCompatWrapper = activityCompatWrapper,
            onRequestPermissionsCompleted = onRequestPermissionsCompleted,
            onPermissionExplanationRequired = onPermissionExplanationRequired,
        )
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun requestPermissions() {
        permissionsUtility.requestPermissions(
            activity = activity,
            permissions = listOf("FirstPermission", "NextPermission", "FinalPermission",)
        )
    }

    @Test
    fun shouldRequestPermissions() {
        permissionsUtility.shouldRequestPermissions()
    }

    @Test
    fun continueRequestingPermissions() {
        permissionsUtility.continueRequestingPermissions(activity)
    }
}