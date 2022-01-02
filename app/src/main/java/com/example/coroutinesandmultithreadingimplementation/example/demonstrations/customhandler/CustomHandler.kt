package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.customhandler

import android.util.Log
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class CustomHandler {

    private val POISON = Runnable { }
    private val mQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()

    init {
        initWorkerThread()
    }

    private fun initWorkerThread() {
        Thread(Runnable {
            Log.d("CustomHandler", "worker (looper) thread initialized")
            while (true) {
                val runnable: Runnable = try {
                    mQueue.take()
                } catch (e: InterruptedException) {
                    return@Runnable
                }
                if (runnable === POISON) {
                    Log.d("CustomHandler", "poison data detected; stopping working thread")
                    return@Runnable
                }
                runnable.run()
            }
        }).start()
    }

    fun stop() {
        Log.d("CustomHandler", "injecting poison data into the queue")
        mQueue.clear()
        mQueue.add(POISON)
    }

    fun post(job: Runnable) {
        mQueue.add(job)
    }

}