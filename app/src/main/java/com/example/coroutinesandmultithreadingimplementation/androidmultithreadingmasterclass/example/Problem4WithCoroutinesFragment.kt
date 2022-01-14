package com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.example

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.coroutinesandmultithreadingimplementation.R
import kotlinx.android.synthetic.main.fragment_problem4_with_coroutines.*
import kotlinx.coroutines.*
import java.math.BigInteger


class Problem4WithCoroutinesFragment : Fragment() {

    private lateinit var computeFactorialUseCase: ComputeFactorialWithKotlinUseCase

    private var job : Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        computeFactorialUseCase = ComputeFactorialWithKotlinUseCase()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_problem4_with_coroutines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCompute.setOnClickListener { _ ->
            if (edtArgument.text.toString().isEmpty()) {
                return@setOnClickListener
            }

            txtResult.text = ""
            btnCompute.isEnabled = false


            val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(btnCompute.windowToken, 0)

            val argument = Integer.valueOf(edtArgument.text.toString())

            job = CoroutineScope(Dispatchers.Main).launch {
                when (val result = computeFactorialUseCase.computeFactorial(argument, getTimeout())) {
                    is ComputeFactorialWithKotlinUseCase.Result.Success -> onFactorialComputed(result.result)
                    is ComputeFactorialWithKotlinUseCase.Result.Timeout -> onFactorialComputationTimedOut()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.apply { cancel() }
    }


    fun onFactorialComputed(result: BigInteger) {
        txtResult.text = result.toString()
        btnCompute.isEnabled = true
    }

    fun onFactorialComputationTimedOut() {
        txtResult.text = "Computation timed out"
        btnCompute.isEnabled = true
    }

    private fun getTimeout() : Int {
        var timeout: Int
        if (edtTimeout.text.toString().isEmpty()) {
            timeout = MAX_TIMEOUT_MS
        } else {
            timeout = Integer.valueOf(edtTimeout.text.toString())
            if (timeout > MAX_TIMEOUT_MS) {
                timeout = MAX_TIMEOUT_MS
            }
        }
        return timeout
    }

    companion object {
        private const val MAX_TIMEOUT_MS = 1000
    }

}

private class ComputeFactorialWithKotlinUseCase {

    sealed class Result {
        class Success(val result: BigInteger) : Result()
        object Timeout : Result()
    }

    suspend fun computeFactorial(argument: Int, timeout: Int) : Result {

        return withContext(Dispatchers.IO) {

            try {
                withTimeout(timeMillis = timeout.toLong()) {

                    val computationRanges = getComputationRanges(argument)

                    val partialProductsForRanges = computePartialProducts(computationRanges)

                    val result = computeFinalResult(partialProductsForRanges)

                    Result.Success(result)
                }
            } catch (e : TimeoutCancellationException) {
                Result.Timeout
            }

        }
    }

    private fun getComputationRanges(factorialArgument: Int) : Array<ComputationRange> {
        val numberOfThreads = getNumberOfThreads(factorialArgument)

        val threadsComputationRanges = Array(numberOfThreads) { ComputationRange(0, 0) }

        val computationRangeSize = factorialArgument / numberOfThreads

        var nextComputationRangeEnd = factorialArgument.toLong()

        for (i in numberOfThreads - 1 downTo 0) {
            threadsComputationRanges[i] = ComputationRange(
                nextComputationRangeEnd - computationRangeSize + 1,
                nextComputationRangeEnd
            )
            nextComputationRangeEnd = threadsComputationRanges[i].start - 1
        }

        // add potentially "remaining" values to first thread's range
        threadsComputationRanges[0] = ComputationRange(1, threadsComputationRanges[0].end)

        return threadsComputationRanges
    }

    private fun getNumberOfThreads(factorialArgument: Int): Int {
        return if (factorialArgument < 20)
            1
        else
            Runtime.getRuntime().availableProcessors()
    }

    private suspend fun computePartialProducts(computationRanges: Array<ComputationRange>) : List<BigInteger> = coroutineScope {
        return@coroutineScope withContext(Dispatchers.IO) {
            return@withContext computationRanges.map {
                computeProductForRangeAsync(it)
            }.awaitAll()
        }
    }

    private fun CoroutineScope.computeProductForRangeAsync(computationRange: ComputationRange) : Deferred<BigInteger> = async(Dispatchers.IO) {
        val rangeStart = computationRange.start
        val rangeEnd = computationRange.end

        var product = BigInteger("1")
        for (num in rangeStart..rangeEnd) {
            if (!isActive) {
                break
            }
            product = product.multiply(BigInteger(num.toString()))
        }

        return@async product
    }

    private suspend fun computeFinalResult(partialProducts: List<BigInteger>): BigInteger = withContext(Dispatchers.IO) {
        var result = BigInteger("1")
        for (partialProduct in partialProducts) {
            if (!isActive) {
                break
            }
            result = result.multiply(partialProduct)
        }
        return@withContext result
    }

    private data class ComputationRange(val start: Long, val end: Long)
}


