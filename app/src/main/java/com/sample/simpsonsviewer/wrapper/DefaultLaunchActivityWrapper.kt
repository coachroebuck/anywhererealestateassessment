package com.sample.simpsonsviewer.wrapper

import android.content.Context
import android.content.Intent
import android.os.Bundle


class DefaultLaunchActivityWrapper : LaunchActivityWrapper {
    override fun <T> navigate(
        applicationContext: Context,
        clazz: Class<T>,
        flags: Int,
        bundle: Bundle?,
        vararg extras: Any?
    ) {
        val intent = Intent(applicationContext, clazz)
        intent.flags = flags

        extras.map { nextObject ->
            nextObject?.let {
                val key = it::class.simpleName
                intent.putExtra(key, it as java.io.Serializable)
            }
        }
        bundle?.let { intent.putExtras(it) }
        applicationContext.startActivity(intent)
    }
}
