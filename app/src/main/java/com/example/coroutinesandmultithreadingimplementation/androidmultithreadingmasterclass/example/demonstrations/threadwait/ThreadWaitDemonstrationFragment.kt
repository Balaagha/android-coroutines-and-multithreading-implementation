package com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.example.demonstrations.threadwait

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import com.example.coroutinesandmultithreadingimplementation.R
import kotlinx.android.synthetic.main.fragment_problem4.*
import java.math.BigInteger
import java.util.*


class ThreadWaitDemonstrationFragment : Fragment() {


    companion object {
        private const val MAX_TIMEOUT_MS = 1000
    }

    private val THREADS_COMPLETION_LOCK = Object()

    private val mUiHandler = Handler(Looper.getMainLooper())

    private var mNumberOfThreads = 0 // safe
    private var mThreadsComputationRanges: Array<ComputationRange?> = arrayOf() // safe

    @Volatile
    private var mThreadsComputationResults: Array<BigInteger?>? = arrayOf() // safe

    private var mNumOfFinishedThreads = 0 // safe

    private var mComputationTimeoutTime: Long = 0

    @Volatile
    private var mAbortComputation = false // safe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_problem4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCompute.setOnClickListener(View.OnClickListener {
            if (edtArgument.text.toString().isEmpty()) {
                return@OnClickListener
            }
            txtResult.text = ""
            btnCompute.isEnabled = false
            val imm =
                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(btnCompute.windowToken, 0)
            val argument: Int = Integer.valueOf(edtArgument.text.toString())
            Log.d("myTag",
                "in onViewCreated| thread name => ${Thread.currentThread().name} | thread id => ${Thread.currentThread().id}")
            computeFactorial(argument, getTimeout())
        })

    }

    override fun onStop() {
        super.onStop()
        mAbortComputation = true
    }

    private fun getTimeout(): Int {
        var timeout: Int
        if (edtTimeout.text.toString().isEmpty()) {
            timeout =
                MAX_TIMEOUT_MS
        } else {
            timeout = Integer.valueOf(edtTimeout.getText().toString())
            if (timeout > MAX_TIMEOUT_MS) {
                timeout =
                    MAX_TIMEOUT_MS
            }
        }
        return timeout
    }

    private fun computeFactorial(factorialArgument: Int, timeout: Int) {
        Thread {
            Log.d("myTag",
                "in computeFactorial| thread name => ${Thread.currentThread().name} | thread id => ${Thread.currentThread().id}")
            initComputationParams(factorialArgument, timeout)
            startComputation()
            waitForThreadsResultsOrTimeoutOrAbort()
            processComputationResults()
        }.start()
    }

    private fun initComputationParams(factorialArgument: Int, timeout: Int) {
        mNumberOfThreads =
            if (factorialArgument < 20) 1 else Runtime.getRuntime().availableProcessors()
        synchronized(THREADS_COMPLETION_LOCK){
            mNumOfFinishedThreads = 0
        }
        mAbortComputation = false

        mThreadsComputationResults = arrayOfNulls(mNumberOfThreads)
        mThreadsComputationRanges = arrayOfNulls(mNumberOfThreads)

        initThreadsComputationRanges(factorialArgument)

        mComputationTimeoutTime = System.currentTimeMillis() + timeout
    }

    private fun initThreadsComputationRanges(factorialArgument: Int) {
        val computationRangeSize = factorialArgument / mNumberOfThreads
        var nextComputationRangeEnd = factorialArgument.toLong()
        for (i in mNumberOfThreads - 1 downTo 0) {
            mThreadsComputationRanges[i] = ComputationRange(
                nextComputationRangeEnd - computationRangeSize + 1,
                nextComputationRangeEnd
            )
            nextComputationRangeEnd = mThreadsComputationRanges[i]!!.start - 1
        }

        // add potentially "remaining" values to first thread's range
        mThreadsComputationRanges[0]!!.start = 1
    }

    @WorkerThread
    private fun startComputation() {
        for (i in 0 until mNumberOfThreads) {
            Thread {
                Log.d("myTag",
                    "in startComputation at start | thread name => ${Thread.currentThread().name} | thread id => ${Thread.currentThread().id}")
                val rangeStart = mThreadsComputationRanges[i]?.start
                val rangeEnd = mThreadsComputationRanges[i]?.end
                var product = BigInteger("1")
                for (num in rangeStart!!..rangeEnd!!) {
                    if (isTimedOut()) {
                        break
                    }
                    product = product.multiply(BigInteger(num.toString()))
                }
                mThreadsComputationResults?.set(i, product)

                synchronized(THREADS_COMPLETION_LOCK) {
                    mNumOfFinishedThreads++
                    Log.d("myTag",
                        "in startComputation at THREADS_COMPLETION_LOCK notifyAll() | thread name => ${Thread.currentThread().name} | thread id => ${Thread.currentThread().id}")
                    THREADS_COMPLETION_LOCK.notify()
                }
            }.start()
        }
    }

    @WorkerThread
    private fun waitForThreadsResultsOrTimeoutOrAbort() {
        synchronized(THREADS_COMPLETION_LOCK) {
            while (mNumOfFinishedThreads != mNumberOfThreads && !mAbortComputation && !isTimedOut()) {
                Log.d("myTag",
                    "in waitForThreadsResultsOrTimeoutOrAbort at synchronized(THREADS_COMPLETION_LOCK) | thread name => ${Thread.currentThread().name} | thread id => ${Thread.currentThread().id}")
                THREADS_COMPLETION_LOCK.wait(getRemainingMillisToTimeout())
            }
        }
    }

    private fun getRemainingMillisToTimeout(): Long {
        return mComputationTimeoutTime - System.currentTimeMillis()
    }

    @WorkerThread
    private fun processComputationResults() {
        var resultString: String
        Log.d("myTag",
            "in processComputationResults at start | thread name => ${Thread.currentThread().name} | thread id => ${Thread.currentThread().id}")
        resultString = if (mAbortComputation) {
            "Computation aborted"
        } else {
            computeFinalResult().toString()
        }

        // need to check for timeout after computation of the final result
        if (isTimedOut()) {
            resultString = "Computation timed out"
        }
        val finalResultString = resultString
        mUiHandler.post {
            if (!this@ThreadWaitDemonstrationFragment.isStateSaved) {
                Log.d("myTag",
                    "in processComputationResults at handler | thread name => ${Thread.currentThread().name} | thread id => ${Thread.currentThread().id}")
                txtResult.text = finalResultString
                btnCompute.isEnabled = true
            }
        }
    }

    @WorkerThread
    private fun computeFinalResult(): BigInteger? {
        var result = BigInteger("1")
        for (i in 0 until mNumberOfThreads) {
            if (isTimedOut()) {
                break
            }
            mThreadsComputationResults?.get(i)?.let {
                result = result.multiply(it)
            }
        }
        return result
    }

    private fun isTimedOut(): Boolean {
        return System.currentTimeMillis() >= mComputationTimeoutTime
    }

    data class ComputationRange(var start: Long, var end: Long)

}