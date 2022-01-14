package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.designthreadcoroutines

import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

class ProducerConsumerBenchmarkUseCase {

    companion object {
        private const val NUM_OF_MESSAGES = 1000
        private const val BLOCKING_QUEUE_CAPACITY = 5
    }

    private val blockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private var numOfReceivedMessages: AtomicInteger = AtomicInteger(0)
    private var numOfProducers: AtomicInteger = AtomicInteger(0)
    private var numOfConsumers: AtomicInteger = AtomicInteger(0)

    // A way for define our own dispatcher
    private val myDispatcher: CoroutineContext = Executors.newCachedThreadPool().asCoroutineDispatcher()

    @Volatile
    private var startTimestamp: Long = 0

    suspend fun startBenchmark(): Result {

        withContext(Dispatchers.IO) {

            numOfReceivedMessages.set(0)

            numOfProducers.set(0)
            numOfConsumers.set(0)

            startTimestamp = System.currentTimeMillis()

            /**
             * We make that two functions with [GlobalScope]
             * Because when we cancel top level coroutines at the fragment, that's not cal cancel
             * [GlobalScope] make sure this Threads are finished
             */
            // producers init coroutine
//            val deferredProducers = GlobalScope.async(Dispatchers.IO ) {
            val deferredProducers = async(Dispatchers.IO + NonCancellable ) {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewProducer(i)
                }
            }

            // consumers init coroutine
            val deferredConsumers = async(Dispatchers.IO + NonCancellable) {
                for (i in 0 until NUM_OF_MESSAGES) {
                    val aaa = startNewConsumer()
                }
            }

            /**
             * We make it because Producers and Consumers launcher Thread is In GlobalScope
             * And at the top level, withContext(Dispatchers.IO) saw it not its scope. and go line return Result , send it to fragment
             * now we launch coroutines with [async] and it return deferred,
             * and below line [awaitAll] deferred and blocked thread for its finished.
             */
             awaitAll(deferredProducers,deferredConsumers) // or defferedProducers.await();defferedConsumers.await()
        }

        return Result(
            System.currentTimeMillis() - startTimestamp,
            numOfReceivedMessages.get()
        )

    }


    private fun CoroutineScope.startNewProducer(index: Int) = launch(Dispatchers.IO)  {
        Log.d("ProducerAndConsumer","producer ${numOfProducers.incrementAndGet()} started; " +
                "on thread ${Thread.currentThread().name}")
        Thread.sleep(10)
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


