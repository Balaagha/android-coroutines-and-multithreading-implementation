package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.designthreadcoroutines

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coroutinesandmultithreadingimplementation.R
import kotlinx.android.synthetic.main.fragment_design_with_thread_demonstration.*
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger


class DesignWithCoroutinesDemonstrationFragment : Fragment() {

    private val mProducerConsumerBenchmarkUseCase by lazy {
        ProducerConsumerBenchmarkUseCase()
    }

    private var showUiNonBlockedIndication : Boolean = false


    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_design_with_thread_demonstration,
            container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnStart.setOnClickListener {
            btnStart.isEnabled = false
            txtReceivedMessagesCount.text = ""
            txtExecutionTime.text = ""
            progressBar.visibility = View.VISIBLE

            job = CoroutineScope(Dispatchers.Main).launch {
                val result = mProducerConsumerBenchmarkUseCase.startBenchmark()
                onBenchmarkCompleted(result)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("myTag","@onStart register listener")
        showUiNonBlockedIndication = true
        postUiNonBlockedIndication()
    }

    override fun onStop() {
        super.onStop()
        Log.d("myTag","@onStop unRegister listener")
        showUiNonBlockedIndication = false
        job?.apply { cancel() }
    }

    private fun postUiNonBlockedIndication() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (showUiNonBlockedIndication) {
                    val indicatorVisible = viewUiNonBlockedIndicator.visibility == View.VISIBLE
                    viewUiNonBlockedIndicator.visibility = if (indicatorVisible) View.INVISIBLE else View.VISIBLE
                    postUiNonBlockedIndication()
                }
            },
            500
        )
    }

    fun onBenchmarkCompleted(result: ProducerConsumerBenchmarkUseCase.Result) {
        Log.d("myTag","@onBenchmarkCompleted ${result}")

        progressBar.visibility = View.INVISIBLE
        btnStart.isEnabled = true
        txtReceivedMessagesCount.text = "Received messages: " + result.numOfReceivedMessages
        txtExecutionTime.text = "Execution time: " + result.executionTime.toString() + "ms"
    }

}