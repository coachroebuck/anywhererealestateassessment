package com.sample.simpsonsviewer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApplication: MainApplication() {
}

open class MainApplication : Application()