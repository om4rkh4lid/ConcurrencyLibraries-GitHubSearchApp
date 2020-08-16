package com.example.lastgithubsearch;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.core.os.HandlerCompat;

public class MyApp extends Application {
    // TODO(1) - Create an instance of an ExecutorService and create a Handler to run tasks on the main thread in your Application class
    /**
     *  from https://android.jlelse.eu/executor-framework-understanding-the-basics-43d575e72310
     *  The Executor framework was introduced to simplify concurrent programming by:
     *  (1)  It lets the developer to create tasks(Runnables, Callables) - the same way you would create an AsyncTask,
     *  and let the framework decide when, how and where to execute that task
     *  on a Thread which is totally configurable.
     *  Previously, you had to write your code inside the run method of the Thread object for example.
     *  (2) Relieves the developer from thread management, e.g. you create the threads once, and reuse them.
     *  (3) It provides the developers various types of queues for storing the tasks.
     *  It also provides various mechanisms for handling the scenario in which a task is rejected by the queue when it is full.
     *  ******************************************************
     *  Mechanism:
     *  The executor picks up a thread from the threadpool to execute a task.
     *  If a thread is not available and new threads cannot be created,
     *  then the executor stores these tasks in a queue.
     *
     *  The executor keeps a minimum number of threads in the thread pool
     *  even if all of them are not executing some task.
     *
     *  The result of the task submitted for execution to an executor can be accessed using the
     *  java.util.concurrent.Future object returned by the executor.
     *  A task submitted to the executor, is asynchronous i.e. the program execution does not wait
     *  for the completion of task execution to proceed to next step.
     *  Instead, whenever the task execution is completed, it is set in this Future object by the executor.
     *  *******************************************************
     *  Usage:
     *  Executor is the base interface of this framework which has only one method execute(Runnable command).
     *  The Executors class provides factory methods for the most common kinds and configurations of Executors,
     *  as well as a few utility methods for using them
     *  ExecutorService and ScheduledExecutorService are two other interfaces which extend Executor.
     *  These two interfaces have a lot of important methods like submit(Runnable task), shutdown(), schedule(Callable<V> callable,long delay, TimeUnit unit) etc...
     *  which actually make this framework really useful.
     *  The most commonly used implementations of these interfaces are ThreadPoolExecutor and ScheduledThreadPoolExecutor.
     *  An executor can be shut down using shutDown() function.
     *  When the executor is shut down, it will no longer accept any new task and submitting any task to
     *  it will throw a RejectedExecutionException. But the tasks already executing on the threads and
     *  stored in the queue for execution will be executed. But if we do not want
     *  to execute the tasks stored in queue, then we need to call shutDownNow().
     *
     * Creating threads is expensive, so you should create a thread pool only once as your app initializes.
     *
     * Application class is a base class of Android app containing components like Activities and Services.
     * Application or its sub classes are instantiated before all the
     * activities or any other application objects have been created in Android app.
     *
     * A Handler allows you to send and process Runnable objects associated with a thread's MessageQueue.
     * A MessageQueue is basically a queue of tasks that are scheduled to run on the thread.
     * Each Handler instance is associated with a single thread and that thread's message queue.
     * When you create a new Handler it is bound to a Looper.
     *
     * You can use a Handler to enqueue an action to be performed on a different thread.
     * To specify the thread on which to run the action, construct the Handler using a Looper for the thread.
     *
     * A Looper is an object that runs the message loop for an associated thread's MessageQueue (basically its constantly looping to execute tasks from the MessageQueue).
     * Looper includes a helper function, getMainLooper(), which retrieves the Looper of the main thread.
     * You can run code in the main thread by using this Looper to create a Handler.
     *
     * Once you've created a Handler, you can then use the post(Runnable) method to run a block of code in the corresponding thread.
     */


    static ExecutorService executorService = Executors.newFixedThreadPool(4);
    static Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());


}
