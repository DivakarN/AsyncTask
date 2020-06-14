package com.sysaxiom.asynctask.looperthread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sysaxiom.asynctask.handlerthread.ExampleHandler.Companion.TASK_A
import com.sysaxiom.asynctask.handlerthread.ExampleHandler.Companion.TASK_B
import android.os.Message
import com.sysaxiom.asynctask.R


class LooperThreadActivity : AppCompatActivity() {

    private val looperThread = ExampleLooperThread()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_looper_thread)
    }

    fun startThread(view: View) {
        looperThread.start()
    }

    fun stopThread(view: View) {
        looperThread.looper?.quit()
    }

    fun taskA(view: View) {
        val msg = Message.obtain()
        msg.what = TASK_A
        looperThread.handler.sendMessage(msg)
    }

    fun taskB(view: View) {
        val msg = Message.obtain()
        msg.what = TASK_B
        looperThread.handler.sendMessage(msg)
    }
}

