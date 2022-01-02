package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.visibility



object VisibilityDemonstration {

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
        Producer().start()
    }

    /**
     * It is wrong way. Because it is possible in the second if check sCount value is changed by Producer
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
     */
    internal class Consumer : Thread() {
        override fun run() {
            var localValue = -1
            while (true) {
                val samplesCount = sCount
                if (localValue != samplesCount) {
                    println("Consumer: detected count change " + samplesCount)
                    localValue = samplesCount
                }
                if (samplesCount >= 5) {
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
        }
    }
}