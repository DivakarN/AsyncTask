package com.sysaxiom.asynctask.handlerthread

import android.os.SystemClock
import android.annotation.SuppressLint
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.os.Process
import android.util.Log


class ExampleHandlerThread : HandlerThread("ExampleHandlerThread", Process.THREAD_PRIORITY_BACKGROUND) {
    var handler: Handler? = null
        private set

    @SuppressLint("HandlerLeak")
    override fun onLooperPrepared() {
        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    EXAMPLE_TASK -> {
                        Log.d(TAG, "Example Task, arg1: " + msg.arg1 + ", obj: " + msg.obj)
                        for (i in 0..3) {
                            Log.d(TAG, "handleMessage: $i")
                            SystemClock.sleep(1000)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = "ExampleHandlerThread"
        val EXAMPLE_TASK = 1
    }
}