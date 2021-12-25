package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.uihandler

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coroutinesandmultithreadingimplementation.R
import kotlinx.android.synthetic.main.fragment_ui_handler_demonstration.*


class UiHandlerDemonstrationFragment : Fragment() {


    private val mUiHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ui_handler_demonstration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_count_iterations.setOnClickListener {
            countIterations()
        }
    }

    private fun countIterations() {
        Thread {
            val startTimestamp = System.currentTimeMillis()
            val endTimestamp = startTimestamp + ITERATIONS_COUNTER_DURATION_SEC * 1000
            var iterationsCount = 0
            while (System.currentTimeMillis() <= endTimestamp) {
                iterationsCount++
            }
            val iterationsCountFinal = iterationsCount

            mUiHandler.post {
                Log.d("mainTag", "Current thread: " + Thread.currentThread().name)
                btn_count_iterations.setText("Iterations: $iterationsCountFinal")
            }
        }.start()
    }

    companion object {
        private const val ITERATIONS_COUNTER_DURATION_SEC = 10

    }

}