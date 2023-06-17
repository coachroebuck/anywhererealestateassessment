package com.sample.simpsonsviewer.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import java.io.Serializable

@OptIn(ExperimentalCoroutinesApi::class)
open class DefaultUnitTest {

    protected lateinit var bundle: Bundle
    protected lateinit var context: Context
    protected lateinit var intent: Intent
    protected val bundleKeySlot = slot<String>()
    protected val bundleIntValueSlot = slot<Int>()
    protected val bundleStringValueSlot = slot<String>()
    protected val bundleSerializableValueSlot = slot<Serializable>()
    protected val bundleSlot = slot<Bundle>()
    protected val extrasSlot = slot<Any>()
    protected val intentSlot = slot<Intent>()
    protected val intSlot = slot<Int>()

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    val coroutineScope = TestScope()

    protected lateinit var activity: Activity

    open fun setUp() {
//        Dispatchers.setMain(mainThreadSurrogate)
        Dispatchers.setMain(StandardTestDispatcher(coroutineScope.testScheduler))

        mockkConstructor(Bundle::class)
        mockkConstructor(Intent::class)

        bundle = mockk(relaxed = true)
        context = mockk(relaxed = true)
        intent = mockk(relaxed = true)
        activity = mockk(relaxed = true)

        every {
            anyConstructed<Bundle>().putInt(
                capture(bundleKeySlot),
                capture(bundleIntValueSlot)
            )
        } answers { }
        every {
            anyConstructed<Bundle>().putString(
                capture(bundleKeySlot),
                capture(bundleStringValueSlot)
            )
        } answers { }
        every {
            anyConstructed<Bundle>().putSerializable(
                capture(bundleKeySlot),
                capture(bundleSerializableValueSlot)
            )
        } answers { }
        coEvery {
            anyConstructed<Bundle>().putInt(
                capture(bundleKeySlot),
                capture(bundleIntValueSlot)
            )
        } answers { }
        coEvery {
            anyConstructed<Bundle>().putString(
                capture(bundleKeySlot),
                capture(bundleStringValueSlot)
            )
        } answers { }
        coEvery {
            anyConstructed<Bundle>().putSerializable(
                capture(bundleKeySlot),
                capture(bundleSerializableValueSlot)
            )
        } answers { }
    }

    open fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    protected fun <T> validateEquality(expectation: T, actual: T, message: String = "") {
        Assert.assertEquals(
            "$message; expected=[${expectation}] actual=[$actual]",
            expectation,
            actual
        )
    }

    protected fun <T> validateInEquality(expectation: T, actual: T) {
        Assert.assertNotEquals(
            "expected=[${expectation}] actual=[$actual]",
            expectation,
            actual
        )
    }

    protected fun <T : Serializable> stubGetSerializableFromBundle(
        key: String?,
        value: T?,
        clazz: Class<T>
    ) {
        every {
            bundle.getSerializable(
                key,
                clazz
            )
        } answers {
            return@answers value
        }
        every {
            bundle.getSerializable(key)
        } answers {
            return@answers value
        }
        coEvery {
            bundle.getSerializable(
                key,
                clazz
            )
        } answers {
            return@answers value
        }
        coEvery {
            bundle.getSerializable(key)
        } answers {
            return@answers value
        }
    }
}
