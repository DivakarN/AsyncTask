package com.sysaxiom.asynctask.handlerthread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.os.Message
import com.sysaxiom.asynctask.R
import com.sysaxiom.asynctask.handlerthread.ExampleHandlerThread.Companion.EXAMPLE_TASK
import kotlinx.android.synthetic.main.activity_handler_thread.*

class HandlerThreadActivity : AppCompatActivity() {

    private val handlerThread = ExampleHandlerThread()
    private val runnable1 =
        ExampleRunnable1()
    private val token = Object()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler_thread)

        handlerThread.start();

        do_work.setOnClickListener {
            val msg = Message.obtain(handlerThread.handler)
            msg.what = EXAMPLE_TASK
            msg.arg1 = 23
            msg.obj = "Obj String"
            msg.sendToTarget()
            handlerThread.handler?.postAtTime(runnable1, token, SystemClock.uptimeMillis())
            handlerThread.handler?.post(runnable1)

        }

        remove_messages.setOnClickListener {
            handlerThread.handler?.removeCallbacks(runnable1, token)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerThread.quit()
    }

    internal class ExampleRunnable1 : Runnable {
        override fun run() {
            for (i in 0..3) {
                Log.d("MainActivity", "Runnable1: $i")
                SystemClock.sleep(1000)
            }
        }
    }

}
