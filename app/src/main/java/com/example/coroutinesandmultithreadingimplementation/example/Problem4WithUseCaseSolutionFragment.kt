package com.example.coroutinesandmultithreadingimplementation.example

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
import com.example.coroutinesandmultithreadingimplementation.common.BaseObservable
import kotlinx.android.synthetic.main.fragment_problem4.*
import java.math.BigInteger


class Problem4WithUseCaseSolutionFragment : Fragment(), ComputeFactorialUseCase.Listener {


    companion object {
        private const val MAX_TIMEOUT_MS = 1000
    }

    private val mComputeFactorialUseCase by lazy {
        ComputeFactorialUseCase()
    }

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


            mComputeFactorialUseCase.computeFactorialAndNotify(argument, getTimeout())
        })

    }

    override fun onStart() {
        super.onStart()
        mComputeFactorialUseCase.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        mComputeFactorialUseCase.unregisterListener(this)
    }

    private fun getTimeout(): Int {
        var timeout: Int
        if (edtTimeout.text.toString().isEmpty()) {
            timeout =
                MAX_TIMEOUT_MS
        } else {
            timeout = Integer.valueOf(edtTimeout.text.toString())
            if (timeout > MAX_TIMEOUT_MS) {
                timeout =
                    MAX_TIMEOUT_MS
            }
        }
        return timeout
    }

    override fun onFactorialComputed(result: BigInteger?) {
        txtResult.text = result.toString()
        btnCompute.isEnabled = true
    }

    override fun onFactorialComputationTimedOut() {
        txtResult.text = "Computation timed out"
        btnCompute.isEnabled = true
    }

    override fun onFactorialComputationAborted() {
        txtResult.text = "Computation aborted"
        btnCompute.isEnabled = true
    }

}




class ComputeFactorialUseCase : BaseObservable<ComputeFactorialUseCase.Listener?>() {

    interface Listener {
        fun onFactorialComputed(result: BigInteger?)
        fun onFactorialComputationTimedOut()
        fun onFactorialComputationAborted()
    }


    private val LOCK = Object()

    private val mUiHandler = Handler(Looper.getMainLooper())

    private var mNumberOfThreads = 0
    private var mThreadsComputationRanges: Array<ComputationRange?> = arrayOf()

    @Volatile
    private  var mThreadsComputationResults: Array<BigInteger?>? = arrayOf()

    private var mNumOfFinishedThreads = 0

    private var mComputationTimeoutTime: Long = 0
    private var mAbortComputation = false

    override fun onLastListenerUnregistered() {
        super.onLastListenerUnregistered()
        synchronized(LOCK) {
            mAbortComputation = true
            LOCK.notifyAll()
        }
    }


    fun computeFactorialAndNotify(argument: Int, timeout: Int) {
        Thread {
            initComputationParams(argument, timeout)
            startComputation()
            waitForThreadsResultsOrTimeoutOrAbort()
            processComputationResults()
        }.start()
    }

    private fun initComputationParams(factorialArgument: Int, timeout: Int) {
        mNumberOfThreads =
            if (factorialArgument < 20) 1 else Runtime.getRuntime().availableProcessors()
        synchronized(LOCK) {
            mNumOfFinishedThreads = 0
            mAbortComputation = false
        }
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
                val rangeStart = mThreadsComputationRanges[i]!!.start
                val rangeEnd = mThreadsComputationRanges[i]!!.end
                var product = BigInteger("1")
                for (num in rangeStart..rangeEnd) {
                    if (isTimedOut) {
                        break
                    }
                    product = product.multiply(BigInteger(num.toString()))
                }
                mThreadsComputationResults?.set(i, product)
                synchronized(LOCK) {
                    mNumOfFinishedThreads++
                    LOCK.notifyAll()
                }
            }.start()
        }
    }

    @WorkerThread
    private fun waitForThreadsResultsOrTimeoutOrAbort() {
        synchronized(LOCK) {
            while (mNumOfFinishedThreads != mNumberOfThreads && !mAbortComputation && !isTimedOut) {
                try {
                    LOCK.wait(remainingMillisToTimeout)
                } catch (e: InterruptedException) {
                    return
                }
            }
        }
    }

    @WorkerThread
    private fun processComputationResults() {
        if (mAbortComputation) {
            notifyAborted()
            return
        }
        val result = computeFinalResult()

        // need to check for timeout after computation of the final result
        if (isTimedOut) {
            notifyTimeout()
            return
        }
        notifySuccess(result)
    }

    @WorkerThread
    private fun computeFinalResult(): BigInteger {
        var result = BigInteger("1")
        for (i in 0 until mNumberOfThreads) {
            if (isTimedOut) {
                break
            }
            mThreadsComputationResults?.get(i)?.let {
                result = result.multiply(it)
            }

        }
        return result
    }

    private val remainingMillisToTimeout: Long
        private get() = mComputationTimeoutTime - System.currentTimeMillis()
    private val isTimedOut: Boolean
        private get() = System.currentTimeMillis() >= mComputationTimeoutTime

    private fun notifySuccess(result: BigInteger) {
        mUiHandler.post {
            for (listener in getListeners()!!) {
                listener?.onFactorialComputed(result)
            }
        }
    }

    private fun notifyAborted() {
        mUiHandler.post {
            for (listener in getListeners()!!) {
                listener?.onFactorialComputationAborted()
            }
        }
    }

    private fun notifyTimeout() {
        mUiHandler.post {
            for (listener in getListeners()!!) {
                listener?.onFactorialComputationTimedOut()
            }
        }
    }

    private data class ComputationRange( var start: Long, var end: Long)
}
