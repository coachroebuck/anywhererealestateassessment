package com.sample.simpsonsviewer.wrapper

import android.content.Context
import androidx.core.content.ContextCompat
import com.sample.simpsonsviewer.wrapper.ContextCompatWrapper

class DefaultContextCompatWrapper : ContextCompatWrapper {
    override fun checkSelfPermission(context: Context, nextPermission: String) =
        ContextCompat.checkSelfPermission(
            context,
            nextPermission
        )
}