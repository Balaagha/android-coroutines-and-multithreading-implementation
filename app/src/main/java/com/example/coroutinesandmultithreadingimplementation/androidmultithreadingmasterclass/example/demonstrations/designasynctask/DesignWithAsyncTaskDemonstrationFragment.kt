package com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.example.demonstrations.designasynctask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coroutinesandmultithreadingimplementation.R
import kotlinx.android.synthetic.main.fragment_design_with_thread_demonstration.*


class DesignWithAsyncTaskDemonstrationFragment : Fragment(),
    ProducerConsumerBenchmarkUseCase.Listener {

    private val mProducerConsumerBenchmarkUseCase by lazy {
        ProducerConsumerBenchmarkUseCase()
    }

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

            mProducerConsumerBenchmarkUseCase.startBenchmarkAndNotify()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("myTag","@onStart register listener")
        mProducerConsumerBenchmarkUseCase.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        Log.d("myTag","@onStop unRegister listener")
        mProducerConsumerBenchmarkUseCase.unregisterListener(this)
    }

    override fun onBenchmarkCompleted(result: ProducerConsumerBenchmarkUseCase.Result) {
        Log.d("myTag","@onBenchmarkCompleted ${result}")

        progressBar.visibility = View.INVISIBLE
        btnStart.isEnabled = true
        txtReceivedMessagesCount.text = "Received messages: " + result.numOfReceivedMessages
        txtExecutionTime.text = "Execution time: " + result.executionTime.toString() + "ms"
    }

}