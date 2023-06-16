package com.sample.simpsonsviewer.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class AppCoroutineScope(
    override val coroutineContext: CoroutineContext = Dispatchers.IO
) : CoroutineScope
