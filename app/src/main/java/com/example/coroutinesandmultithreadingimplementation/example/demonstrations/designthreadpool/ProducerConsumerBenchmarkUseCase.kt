package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.designthreadpool

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.coroutinesandmultithreadingimplementation.common.BaseObservable
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

class ProducerConsumerBenchmarkUseCase :
    BaseObservable<ProducerConsumerBenchmarkUseCase.Listener>() {

    private val LOCK = Object()

    private val NUM_OF_MESSAGES = 10000
    private val BLOCKING_QUEUE_CAPACITY = 5

    private val mUiHandler = Handler(Looper.getMainLooper())

    private val mBlockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private val mNumOfThreads = AtomicInteger(0)


    //    private lateinit var mThreadPool: ExecutorService
    /**
     * when we call newCachedThreadPool, it means that this thread pool will create a new threads whenever needed. But then after the operation completes, it will use the same thread
     * But we use more threads(E.g. 100000) we see outofmemory error
        private val mThreadPool: ExecutorService = Executors.newCachedThreadPool { r ->
        Log.d("ThreadFactory", "thread: " + mNumOfThreads.incrementAndGet())
        Thread(r)
     * For Solution it we can use newFixedThreadPool,but it not work as we want, because
        private val mThreadPool: ExecutorService = Executors.newFixedThreadPool (1000 )

    }
     */

    /**
     * @params
     * corePoolSize => ?
     * maximumPoolSize => It defines the maximum number of threads that will be available inside this  Thread pool
     * keepAliveTime => It defines the time that each specific thread will be kept alive inside this thread pool after it is no longer in use,
     *
     */
    private var mThreadPool: ThreadPoolExecutor = ThreadPoolExecutor(
        1000,
        Int.MAX_VALUE,
        0,
        TimeUnit.SECONDS,
        SynchronousQueue()
    ) { r ->
        Log.d("ThreadFactory", "thread: " + mNumOfThreads.incrementAndGet())
        Thread(r)
    }

    private var mNumOfFinishedConsumers = 0
    private var mNumOfReceivedMessages = 0
    private var mStartTimestamp: Long = 0

    fun startBenchmarkAndNotify() {
        synchronized(LOCK) {
//            mThreadPool = Executors.newCachedThreadPool { r ->
//                Log.d("ThreadFactory", "thread: " + mNumOfThreads.incrementAndGet())
//                Thread(r)
//            }

            mNumOfReceivedMessages = 0
            mNumOfFinishedConsumers = 0
            mStartTimestamp = System.currentTimeMillis()
            mNumOfThreads.set(0)
        }

        // watcher-reporter thread
        mThreadPool.execute(
            Runnable {
                synchronized(LOCK) {
                    while (mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                        try {
                            LOCK.wait()
                        } catch (e: InterruptedException) {
                            return@Runnable
                        }
                    }
                }
                notifySuccess()
            }
        )

        // producers init thread
        mThreadPool.execute {
            for (i in 0 until NUM_OF_MESSAGES) {
                startNewProducer(i)
            }
        }

        // consumers init thread
        mThreadPool.execute {
            for (i in 0 until NUM_OF_MESSAGES) {
                startNewConsumer()
            }
        }
    }

    private fun startNewProducer(index: Int) {
        mThreadPool.execute { mBlockingQueue.put(index) }
    }

    private fun startNewConsumer() {
        mThreadPool.execute {
            val message = mBlockingQueue.take()!!
            synchronized(LOCK) {
                if (message != -1) {
                    mNumOfReceivedMessages++
                }
                mNumOfFinishedConsumers++
                LOCK.notifyAll()
            }
        }
    }

    private fun notifySuccess() {
        mUiHandler.post {
            var result: Result
            synchronized(LOCK) {
                result =
                    Result(
                        System.currentTimeMillis() - mStartTimestamp,
                        mNumOfReceivedMessages
                    )
            }
            Log.d("myTag", "@notifySuccess pass synchronized")
            getListeners()?.let { listeners ->
                Log.d("myTag", "@notifySuccess in have listener")
                for (listener in listeners) {
                    Log.d("myTag", "@notifySuccess if have listener in for")
                    listener.onBenchmarkCompleted(result)
                }
            }
        }
    }

    class Result(val executionTime: Long, val numOfReceivedMessages: Int)

    interface Listener {
        fun onBenchmarkCompleted(result: Result)
    }

}