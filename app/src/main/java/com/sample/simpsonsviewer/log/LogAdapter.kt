package com.sample.simpsonsviewer.log

interface LogAdapter {
    fun v(message: String?)
    fun e(message: String?)
    fun w(message: String?)
    fun wtf(message: String?)
    fun i(message: String?)
    fun d(message: String?)
    fun entering()
    fun exiting()

    companion object {
        const val minimumLevels = 5
        const val targetIndex = 4

        const val Verbose = "AppVerbose"
        const val Info = "AppInfo"
        const val Debug = "AppDebug"
        const val Warning = "AppWarning"
        const val Error = "AppError"
        const val WTF = "AppWTF"
    }
}