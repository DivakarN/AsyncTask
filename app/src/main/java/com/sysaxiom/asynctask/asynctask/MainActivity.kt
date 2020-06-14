package com.sysaxiom.asynctask.asynctask

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sysaxiom.asynctask.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {

    lateinit var myAsynTask : MyAsynTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myAsynTask = MyAsynTask(this)
        myAsynTask.execute()

    }

    inner class MyAsynTask(activity: MainActivity) : AsyncTask<Unit, Int, String>() {

        var mActivityRef: WeakReference<MainActivity> = WeakReference(activity)

        override fun onPreExecute() {
            super.onPreExecute()
            Toast.makeText(this@MainActivity,"Started",Toast.LENGTH_LONG).show()
        }


        override fun doInBackground(vararg p0: Unit?): String {
            for(i in 1..10){
                Thread.sleep(1000)
                publishProgress(i)
            }
            return "Completed"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(mActivityRef.get()!=null){
                Toast.makeText(this@MainActivity,result,Toast.LENGTH_LONG).show()
            }
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            if(mActivityRef.get()!=null){
                textViewCount.text = values.get(0).toString()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        myAsynTask.cancel(true)
    }


}



