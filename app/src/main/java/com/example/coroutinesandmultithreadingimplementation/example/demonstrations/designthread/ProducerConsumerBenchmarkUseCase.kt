package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.designthread

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.coroutinesandmultithreadingimplementation.common.BaseObservable

class ProducerConsumerBenchmarkUseCase :
    BaseObservable<ProducerConsumerBenchmarkUseCase.Listener>() {

    private val LOCK = Object()

    private val NUM_OF_MESSAGES = 1000
    private val BLOCKING_QUEUE_CAPACITY = 5

    private val mUiHandler = Handler(Looper.getMainLooper())

    private val mBlockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private var mNumOfFinishedConsumers = 0
    private var mNumOfReceivedMessages = 0
    private var mStartTimestamp: Long = 0

    fun startBenchmarkAndNotify() {
        synchronized(LOCK) {
            mNumOfReceivedMessages = 0
            mNumOfFinishedConsumers = 0
            mStartTimestamp = System.currentTimeMillis()
        }

        // watcher-reporter thread
        Thread(Runnable {
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
        }).start()

        // producers init thread
        Thread {
            for (i in 0 until NUM_OF_MESSAGES) {
                startNewProducer(i)
            }
        }.start()

        // consumers init thread
        Thread {
            for (i in 0 until NUM_OF_MESSAGES) {
                startNewConsumer()
            }
        }.start()
    }

    private fun startNewProducer(index: Int) {
        Thread { mBlockingQueue.put(index) }.start()
    }

    private fun startNewConsumer() {
        Thread {
            val message = mBlockingQueue.take()!!
            synchronized(LOCK) {
                if (message != -1) {
                    mNumOfReceivedMessages++
                }
                mNumOfFinishedConsumers++
                LOCK.notifyAll()
            }
        }.start()
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
            Log.d("myTag","@notifySuccess pass synchronized")
            getListeners()?.let { listeners ->
                Log.d("myTag","@notifySuccess in have listener")
                for (listener in listeners) {
                    Log.d("myTag","@notifySuccess if have listener in for")
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