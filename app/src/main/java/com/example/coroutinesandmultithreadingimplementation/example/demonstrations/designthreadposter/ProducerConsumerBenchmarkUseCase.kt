package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.designthreadposter

import android.util.Log
import com.example.coroutinesandmultithreadingimplementation.common.BaseObservable
import com.techyourchance.threadposter.BackgroundThreadPoster
import com.techyourchance.threadposter.UiThreadPoster
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

class ProducerConsumerBenchmarkUseCase :
    BaseObservable<ProducerConsumerBenchmarkUseCase.Listener>() {

    private val LOCK = Object()

    private val NUM_OF_MESSAGES = 1000
    private val BLOCKING_QUEUE_CAPACITY = 5

    private val mBlockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private val mNumOfThreads = AtomicInteger(0)


    private val mUiThreadPoster = UiThreadPoster()
    private val mBackgroundThreadPoster = BackgroundThreadPoster()


    private var mNumOfFinishedConsumers = 0
    private var mNumOfReceivedMessages = 0
    private var mStartTimestamp: Long = 0

    fun startBenchmarkAndNotify() {
        synchronized(LOCK) {
            mNumOfReceivedMessages = 0
            mNumOfFinishedConsumers = 0
            mStartTimestamp = System.currentTimeMillis()
            mNumOfThreads.set(0)
        }

        // watcher-reporter thread

        // watcher-reporter thread
        mBackgroundThreadPoster.post {
            synchronized(LOCK) {
                while (mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                    try {
                        LOCK.wait()
                    } catch (e: InterruptedException) {
                        return@post
                    }
                }
            }
            notifySuccess()
        }

        // producers init thread
        mBackgroundThreadPoster.post {
            for (i in 0 until NUM_OF_MESSAGES) {
                startNewProducer(i)
            }
        }

        // consumers init thread
        mBackgroundThreadPoster.post {
            for (i in 0 until NUM_OF_MESSAGES) {
                startNewConsumer()
            }
        }
    }

    private fun startNewProducer(index: Int) {
        mBackgroundThreadPoster.post { mBlockingQueue.put(index) }
    }

    private fun startNewConsumer() {
        mBackgroundThreadPoster.post {
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
        mUiThreadPoster.post {
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