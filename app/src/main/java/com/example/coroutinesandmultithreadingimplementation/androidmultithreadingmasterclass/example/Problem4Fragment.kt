package com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.example

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.WorkerThread
import com.example.coroutinesandmultithreadingimplementation.R
import kotlinx.android.synthetic.main.fragment_problem4.*
import java.math.BigInteger
import java.util.concurrent.atomic.AtomicInteger


class Problem4Fragment : Fragment() {


    companion object {
        private const val MAX_TIMEOUT_MS = 1000
    }

    private val mUiHandler = Handler(Looper.getMainLooper())

    private var mNumberOfThreads = 0 // safe
    private var mThreadsComputationRanges: Array<ComputationRange?> = arrayOf() // safe
    @Volatile
    private var mThreadsComputationResults: Array<BigInteger?>? = arrayOf() // safe

    private val mNumOfFinishedThreads = AtomicInteger(0) // safe

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
            initComputationParams(factorialArgument, timeout)
            startComputation()
            waitForThreadsResultsOrTimeoutOrAbort()
            processComputationResults()
        }.start()
    }

    private fun initComputationParams(factorialArgument: Int, timeout: Int) {
        mNumberOfThreads = if (factorialArgument < 20) 1 else Runtime.getRuntime().availableProcessors()
        mNumOfFinishedThreads.set(0)
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
                mNumOfFinishedThreads.incrementAndGet()
            }.start()
        }
    }

    @WorkerThread
    private fun waitForThreadsResultsOrTimeoutOrAbort() {
        while (true) {
            if (mNumOfFinishedThreads.get() == mNumberOfThreads) {
                break
            } else if (mAbortComputation) {
                break
            } else if (isTimedOut()) {
                break
            } else {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    // do nothing and keep looping
                }
            }
        }
    }

    @WorkerThread
    private fun processComputationResults() {
        var resultString: String
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
            if (!this@Problem4Fragment.isStateSaved) {
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

    data class ComputationRange( var start: Long, var end: Long)

}