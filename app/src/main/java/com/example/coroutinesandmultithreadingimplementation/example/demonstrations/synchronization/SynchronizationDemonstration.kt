package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.synchronization

object SynchronizationDemonstration {
    private val LOCK = Object()
    private var sCount = 0

    @JvmStatic
    fun main(args: Array<String>) {
        Consumer().start()
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            return
        }
        Producer().start()
    }


    internal class Consumer : Thread() {
        override fun run() {
            var localValue = -1
            run infinityLoop@{
                while (true) {
                    synchronized(LOCK) {
                        if (localValue != sCount) {
                            println("Consumer: detected count change " + sCount)
                            localValue = sCount
                        }

                        if (sCount >= 5) {
                            return@infinityLoop
                        }
                    }
                }
            }

            println("Consumer: terminating")
        }
    }

    internal class Producer : Thread() {
        override fun run() {
            run infinityLoop@{
                while (true) {
                    synchronized(LOCK) {
                        if (sCount >= 5) {
                            return@infinityLoop
                        }
                        var localValue = sCount
                        localValue++
                        println("Producer: incrementing count to $localValue")
                        sCount = localValue

                    }
                    try {
                        sleep(1000)
                    } catch (e: InterruptedException) {
                        return
                    }
                }
            }
            println("Producer: terminating")
        }
    }
}