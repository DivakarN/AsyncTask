package com.sysaxiom.asynctask.threading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.util.Log
import android.view.View
import com.sysaxiom.asynctask.R

class ThreadingActivity : AppCompatActivity() {

    lateinit var buttonStartThread: Button
    private val mainHandler = Handler()
    @Volatile
    private var stopThread = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_threading)
        buttonStartThread = findViewById(R.id.button_start_thread)
    }

    fun startThread(view: View) {
        stopThread = false
        val runnable = ExampleRunnable(10)
        Thread(runnable).start()
        /*
        ExampleThread thread = new ExampleThread(10);
        thread.start();
        */
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                //work
            }
        }).start();
        */
    }

    fun stopThread(view: View) {
        stopThread = true
    }

    internal inner class ExampleThread(var seconds: Int) : Thread() {
        override fun run() {
            for (i in 0 until seconds) {
                Log.d("MainActivity", "startThread: $i")
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
    }

    internal inner class ExampleRunnable(var seconds: Int) : Runnable {
        override fun run() {
            for (i in 0 until seconds) {
                if (stopThread)
                    return
                if (i == 5) {
                    /*
                    Handler threadHandler = new Handler(Looper.getMainLooper());
                    threadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                    */
                    /*
                    buttonStartThread.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                    */
                    runOnUiThread { buttonStartThread.text = "50%" }
                }
                Log.d("MainActivity", "startThread: $i")
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
    }

}
