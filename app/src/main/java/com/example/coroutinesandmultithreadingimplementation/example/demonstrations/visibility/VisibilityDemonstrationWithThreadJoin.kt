package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.visibility



object VisibilityDemonstrationWithThreadJoin {

    private val LOCK = Object()
    /**
     * If we dont use Volatile annotation, at the [Consumer] thread never terminated.
     */
    @Volatile
    private var sCount = 0

    @JvmStatic
    fun main(args: Array<String>) {
        Consumer().start()
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            return
        }
        val producer = Producer()
        producer.start()
        /**
         * with join() make main thread wait for [Producer] is terminated
         * And we can remove all synchronized line and uncomment producer.join() method call, so we reach same effect
         */
//        producer.join()
        synchronized(LOCK){
            LOCK.wait()
        }
        println("Main: returns")
    }

    internal class Consumer : Thread() {
        override fun run() {
            var localValue = -1
            while (true) {
                if (localValue != sCount) {
                    println("Consumer: detected count change " + sCount)
                    localValue = sCount
                }
                if (sCount >= 5) {
                    break
                }
            }
            println("Consumer: terminating")
        }
    }

    internal class Producer : Thread() {
        override fun run() {
            while (sCount < 5) {
                var localValue = sCount
                localValue++
                println("Producer: incrementing count to $localValue")
                sCount = localValue
                try {
                    sleep(1000)
                } catch (e: InterruptedException) {
                    return
                }
            }
            println("Producer: terminating")
            synchronized(LOCK){
                LOCK.notifyAll()
            }
        }
    }
}