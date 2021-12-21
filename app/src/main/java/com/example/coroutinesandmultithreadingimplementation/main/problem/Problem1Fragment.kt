package com.example.coroutinesandmultithreadingimplementation.main.problem

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coroutinesandmultithreadingimplementation.R
import kotlinx.android.synthetic.main.fragment_problem1.*


class Problem1Fragment : Fragment() {
    private val ITERATIONS_COUNTER_DURATION_SEC = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_problem1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnMain.setOnClickListener {
            countIterations()
        }
        btnSecondary.setOnClickListener {
            countIterationsWithSafe()
        }
    }

    private fun countIterations() {
        val startTimestamp = System.currentTimeMillis()
        val endTimestamp: Long =
            startTimestamp + ITERATIONS_COUNTER_DURATION_SEC * 1000

        var iterationsCount = 0
        while (System.currentTimeMillis() <= endTimestamp) {
            iterationsCount++
        }

        Log.d(
            "myTag",
            "iterations in " + ITERATIONS_COUNTER_DURATION_SEC + "seconds: " + iterationsCount
        )
    }

    private fun countIterationsWithSafe() {
        Thread {
            val startTimestamp = System.currentTimeMillis()
            val endTimestamp: Long =
                startTimestamp + ITERATIONS_COUNTER_DURATION_SEC * 1000

            var iterationsCount = 0
            while (System.currentTimeMillis() <= endTimestamp) {
                iterationsCount++
            }
            Log.d(
                "myTag",
                "iterations in " + ITERATIONS_COUNTER_DURATION_SEC + "seconds: " + iterationsCount
            )
        }.start()
    }



}