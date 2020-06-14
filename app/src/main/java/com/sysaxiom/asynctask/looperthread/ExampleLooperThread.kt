package com.sysaxiom.asynctask.looperthread

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.sysaxiom.asynctask.handlerthread.ExampleHandler


class ExampleLooperThread : Thread() {
    var looper: Looper? = null
    lateinit var handler: Handler
    override fun run() {
        Looper.prepare()
        looper = Looper.myLooper()
        handler = ExampleHandler()
        Looper.loop()
        Log.d(TAG, "End of run()")
    }

    companion object {
        private val TAG = "ExampleLooperThread"
    }
}