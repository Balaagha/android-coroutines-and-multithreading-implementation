package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.designasynctask

import java.util.*

class MyBlockingQueue(private val mCapacity: Int) {
    private val QUEUE_LOCK = Object()

    private val mQueue: Queue<Int> = LinkedList()
    private var mCurrentSize = 0

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * for space to become available.
     *
     * @param number the element to add
     */
    fun put(number: Int) {
        synchronized(QUEUE_LOCK){
            while (mCurrentSize >= mCapacity){
                QUEUE_LOCK.wait()
            }
            mQueue.offer(number)
            mCurrentSize++
            QUEUE_LOCK.notifyAll()
        }
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary
     * until an element becomes available.
     *
     * @return the head of this queue
     */
    fun take(): Int? {
        synchronized(QUEUE_LOCK){
            while (mCurrentSize <= 0){
                QUEUE_LOCK.wait()
            }
            mCurrentSize--
            QUEUE_LOCK.notifyAll()
            return mQueue.poll()
        }
    }
}