package com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.example

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coroutinesandmultithreadingimplementation.R
import com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.common.DefaultConfiguration
import kotlinx.android.synthetic.main.fragment_problem5.*
import java.util.*


class Problem5Fragment : Fragment() {
    companion object {
        private val NUM_OF_MESSAGES: Int = DefaultConfiguration.DEFAULT_NUM_OF_MESSAGES
        private val BLOCKING_QUEUE_CAPACITY: Int = DefaultConfiguration.DEFAULT_BLOCKING_QUEUE_SIZE
    }

    private val LOCK = Object()

    private val mUiHandler = Handler(Looper.getMainLooper())

    private val mBlockingQueue: MyBlockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private var mNumOfFinishedConsumers = 0

    private var mNumOfReceivedMessages = 0

    private var mStartTimestamp: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_problem5, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnStart.setOnClickListener(View.OnClickListener {
            btnStart.isEnabled = false
            txtReceivedMessagesCount.text = ""
            txtExecutionTime.text = ""
            progress.visibility = View.VISIBLE
            mNumOfReceivedMessages = 0
            mNumOfFinishedConsumers = 0
            startCommunication()
        })
    }

    private fun startCommunication() {
        mStartTimestamp = System.currentTimeMillis()

        // watcher-reporter thread
        Thread {
            synchronized(LOCK) {
                while (mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                    LOCK.wait()
                }
            }
            showResults()
        }.start()

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
        Thread {
            mBlockingQueue.put(index)
        }.start()
    }

    private fun startNewConsumer() {
        Thread {
            val message = mBlockingQueue.take()
            synchronized(LOCK) {
                if (message != -1) {
                    mNumOfReceivedMessages++
                }
                mNumOfFinishedConsumers++
                LOCK.notifyAll()
            }
        }.start()
    }

    private fun showResults() {
        mUiHandler.post {
            progress.visibility = View.INVISIBLE
            btnStart.isEnabled = true
            synchronized(LOCK) {
                txtReceivedMessagesCount.setText("Received messages: $mNumOfReceivedMessages")
            }
            val executionTimeMs = System.currentTimeMillis() - mStartTimestamp
            txtExecutionTime.text = "Execution time: " + executionTimeMs + "ms"
        }
    }

    /**
     * Simplified implementation of blocking queue.
     */
    private class MyBlockingQueue(private val mCapacity: Int) {
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

}