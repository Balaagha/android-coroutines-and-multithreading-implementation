package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.designthreadcoroutines

import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

class ProducerConsumerBenchmarkUseCase {

    companion object {
        private const val NUM_OF_MESSAGES = 1000
        private const val BLOCKING_QUEUE_CAPACITY = 5
    }

    private val blockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private var numOfReceivedMessages: AtomicInteger = AtomicInteger(0)
    private var numOfProducers: AtomicInteger = AtomicInteger(0)
    private var numOfConsumers: AtomicInteger = AtomicInteger(0)

    @Volatile
    private var startTimestamp: Long = 0

    suspend fun startBenchmark(): Result {

        withContext(Dispatchers.IO) {

            numOfReceivedMessages.set(0)

            numOfProducers.set(0)
            numOfConsumers.set(0)

            startTimestamp = System.currentTimeMillis()

            // producers init coroutine
            launch(Dispatchers.IO) {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewProducer(i)
                }
            }

            // consumers init coroutine
            launch(Dispatchers.IO) {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewConsumer()
                }
            }
        }

        return Result(
            System.currentTimeMillis() - startTimestamp,
            numOfReceivedMessages.get()
        )

    }


    private fun CoroutineScope.startNewProducer(index: Int) = launch(Dispatchers.IO)  {
        Thread.sleep(10)
        Log.d("ProducerAndConsumer","producer ${numOfProducers.incrementAndGet()} started; " +
                "on thread ${Thread.currentThread().name}")
        blockingQueue.put(index)
    }

    private fun CoroutineScope.startNewConsumer() = launch(Dispatchers.IO) {
        Log.d("ProducerAndConsumer","consumer ${numOfConsumers.incrementAndGet()} started; " +
                "on thread ${Thread.currentThread().name}")
        val message = blockingQueue.take()
        if (message != -1) {
            numOfReceivedMessages.incrementAndGet()
        }

    }


    class Result(val executionTime: Long, val numOfReceivedMessages: Int)

}


