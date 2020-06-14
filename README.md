# AsyncTask

Process & Thread:
-----------------

Process:
--------

Process = set of program + 'n' number of threads

Difference :

Process is heavy weight and thread is light weight process.
Process shares the data & resources with processor memory and thread shares data & resources with process memory.
Threads can interact with each other & but process cannot interact with others (exception in some cases).
If process killed then process will be blocked and if thread killed then other thread can run in parallel.

Thread:
-------

Execution of a program is called as thread.
Application may have 'n' number of thread
Every application should have one non daemon thread (main thread) and may have 'n' number daemon thread.
Thread can be stopped by using exit method

There are two ways to create a thread.

1) Extending Thread class - overrides run method 
------------------------------------------------

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

ExampleThread thread = new ExampleThread(10)
thread.start()


2) Implementing Runnable interface - overrides run method
---------------------------------------------------------

internal inner class ExampleRunnable(var seconds: Int) : Runnable {
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

val runnable = ExampleRunnable(10)
Thread(runnable).start()


Thread execution in Android:
----------------------------

Generally Java will execute thread and then stops once its done with the execution.
So To keep a thread for longer time, it needs additional work. This can be done by using Looper, Handler, MessageQueue and HandlerThread.
So Basically, Android creates thread and put it inside a looper, so it will keep on running. And every task can be processed in the thread by using Handler.

Looper:
-------

A thread will be run inside a looper to serve the purpose of alive for a long time without stopping.
It dequeue a messages and send them to corresponding handlers or execute runnable using a thread

MessageQueue:
-------------

Its queue of tasks to be performed on the thread by handler. There is two things in message queue

Message - Its a message to be posted in the thread by handler.
Runnable - Its a runnable task to be performed by the handler in the thread.

Handler:
--------

It puts messages and runnable tasks into thread via looper.

Thread with Custom Looper:
--------------------------

We can create a custom looper by using Looper class. 
Looper.prepare will create a looper for the thread.
Looper.loop will put task & messages inside the looper.
Looper.quit will quit the looper object.

public class LooperThread extends Thread
{
	private static final String TAG = LooperThread.class.getSimplename();

	Handler handler;

	override public void run() { 
		Looper.prepare();

		handler = new Handler(){
			public void handleMessage (Messages msg){
				super.handleMessage(msg);
			}
		};

		Looper.loop();
	}
}

HandlerThread:
--------------

HandlerThread is custom class will take care of a looper in a thread for us.

public class CustomHandlerThread extends HandlerThread
{
	private static final String TAG = CustomHandlerThread.class.getSimplename();

	Handler handler;

	protected void onLooperPrepared() { 
		super.onLooperPrepared();
		handler = new Handler(){
			public void handleMessage (Messages msg){
				super.handleMessage(msg);
			}
		};
	}
}

AsyncTask:
----------

Problem Statement:
Android runs on the main/ui thread. So there may be a scenario which we shouldn't block main thread, and run the task in parallel, then update the UI via UI thread. In Android thread communication is always tricky.

To solve this problem, android introduced AsyncTask. It will create a background thread and run parallel in the background and then update the UI via UI thread.

To achieve this, we have to create inner class extending AsyncTask Class.

It has three arguments,
params - its the parameter to the background task.
progress - its the progress type of the async task running in the background.
result - Its the result type of the background task.

It has four methods,
onPreExecute() - runs in the UI thread. If a certain task to be performed before the AsyncTask, then it can be done using this method.         				 
doInBackground(params) - runs in the background thread. It runs code inside doInBackground method in a background thread.
onProgressUpdate(progress) - runs in the UI thread. Progress can be updated in the UI by this method. Using pubishProgress() method we can publish the progress of the task from doInBackground() to onProgressUpdate().
onPostExecute(result) - runs in the UI thread. If a certain task to be performed after the AsyncTask then it can be done using this method.

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

//Executing AsyncTask
val myAsynTask = MyAsynTask(this)
myAsynTask.execute()

Disadvantage: Its meant for shorter operation(like 5 to 10 sec operation in the background : For example, network operation). If we need to perform long operation in the background then we need to go executors.

How to Avoid memory leaks in AsyncTask:
1) To cancel the AsyncTask in the onDestroy() method.
2) To use weakReference object inside the AsyncTask subclass.

Executors:
----------

The most important feature of this framework is the separation of concerns. It lets the developer to create tasks(Runnables, Callables), and let the framework decide when, how and where to execute that task on a Thread which is totally configurable.
It relieves the developer from thread management.
It provides the developers various types of queues for storing the tasks. It also provides various mechanisms for handling the scenario in which a task is rejected by the queue when it is full.

How does Executor work?
To simply put, the work of an executor is to execute tasks . The executor picks up a thread from the threadpool to execute a task. If a thread is not available and new threads cannot be created, then the executor stores these tasks in a queue. A task can also be removed from the queue. If the queue is full, then the queue will start rejecting the tasks.(Rejection of tasks can be handled).

ThreadPoolExecutor:
-------------------

Executor is the base interface of this framework which has only one method execute(Runnable command). ExecutorService and ScheduledExecutorService are two other interfaces which extends Executor. These two interfaces have a lot of important methods like submit(Runnable task), shutdown(), schedule(Callable<V> callable,long delay, TimeUnit unit) etc which actually make this framework really useful. The most commonly used implementations of these interfaces are ThreadPoolExecutor and ScheduledThreadPoolExecutor.

public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          RejectedExecutionHandler handler) {}
						  
corePoolSize : The minimum number of threads to keep in the pool.
maximumPoolSize : The maximum number of threads to keep in the pool.
keepAliveTime : If current number of threads are greater than the minimum threads, then wait for this time to terminate the extra threads.
unit: The time unit for the previous argument.
workQueue : The queue used for holding the tasks.
handler : An instance of RejectionExecutionHandler, which handles the task which is rejected by the executor.

public class StringPrinter {

  private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 0,
      TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
  
   public static void main(String[] args) {
    new StringPrinter().printString();
  }

  public void printString() {
    for (int i = 1; i <= 6; i++) {
      threadPoolExecutor.execute(getRunnable(i));
    }
  }

  private Runnable getRunnable(final int i) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        String randomString = RandomClass.getRandomString(i);
        System.out.println("String returned is : + randomString);
      }
    };
    return runnable;
  }
}


ScheduledThreadPoolExecutor:
----------------------------

This executor can be used when we want to schedule the tasks.
There are times when we want to the repeat the execution of a same task at certain time intervals- e.g. Executing a task after every 5 seconds.

schedule(Runnable command, long delay, TimeUnit unit) : This will execute the task after the delay has expired. So let’s say that you have to execute a task after 10 seconds use schedule(runnable, 10, TimeUnit.SECONDS).
scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) : For the first time, the runnable will be executed after the initialDelay has expired. After that, every time that Runnable will be run after the given period (3rd argument) has expired. Let’s say that you want to run a task after every 10 seconds. Using schedule(runnable, 0, 10, TimeUnit.SECONDS) , the Runnable will execute immediately for the first time, and from there on it will be executed after every 10 seconds. The time interval between executing tasks does not depend upon the time taken by the task itself to complete.
scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) : Although this function may seem similar to the above function, there is a difference between them. Unlike the previous function, this function waits for the previously executing task to complete and only after delay period (3rd argument) has expired, it will execute the task next time.

Shutting down an Executor:
--------------------------

An executor can be shut down using shutDown() function. When the executor is shut down, it will no longer accept any new task and submitting any task to it will throw a RejectedExecutionException. But the tasks already executing on the threads and stored in the queue for execution will be executed. But if we do not want to execute the tasks stored in queue, then we need to call shutDownNow(). Calling this method will make sure that any task stored in the queue should be not executed.
