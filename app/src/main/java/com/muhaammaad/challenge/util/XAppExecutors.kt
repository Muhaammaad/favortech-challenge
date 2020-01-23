package com.muhaammaad.challenge.util

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class XAppExecutors private constructor(
    private val mDiskIO: Executor = Executors.newSingleThreadExecutor(),
    private val mMainThread: Executor = MainThreadExecutor()
) {

    fun diskIO(): Executor {
        return mDiskIO
    }

    fun mainThread(): Executor {
        return mMainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    private object Loader {
        @Volatile
        internal var INSTANCE = XAppExecutors()
    }

    companion object {

        val instance: XAppExecutors
            get() = Loader.INSTANCE
    }

}
