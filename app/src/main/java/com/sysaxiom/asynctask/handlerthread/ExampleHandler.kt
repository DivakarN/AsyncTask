package com.sysaxiom.asynctask.handlerthread

import android.os.Handler
import android.os.Message
import android.util.Log

class ExampleHandler : Handler() {
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            TASK_A -> Log.d(TAG, "Task A executed")
            TASK_B -> Log.d(TAG, "Task B executed")
        }
    }

    companion object {
        private val TAG = "ExampleHandler"
        val TASK_A = 1
        val TASK_B = 2
    }
}