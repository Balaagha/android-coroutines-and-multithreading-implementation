package com.example.coroutinesandmultithreadingimplementation.example

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coroutinesandmultithreadingimplementation.R
import com.example.coroutinesandmultithreadingimplementation.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_problem2.*
import java.util.concurrent.atomic.AtomicBoolean


class Problem2Fragment : BaseFragment() {

    private var mDummyData: ByteArray? = null

    private val mCountAbort = AtomicBoolean(false)

    private var changeCountAbortStateOnStopForSolution = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mDummyData = ByteArray(400 * 1000 * 1000)
        return inflater.inflate(R.layout.fragment_problem2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositionRoot.getToolbarManipulator()?.setScreenTitle("Problem 2")
        mainBtn.setOnClickListener {
            changeCountAbortStateOnStopForSolution = true
        }
    }

    override fun onStart() {
        super.onStart()
        countScreenTime()
    }

    override fun onStop() {
        super.onStop()
        if(changeCountAbortStateOnStopForSolution){
            mCountAbort.set(true)
        }
    }

    private fun countScreenTime() {
        mCountAbort.set(false)
        Thread(Runnable {
            val referenceDummyData = mDummyData
            var screenTimeSeconds = 0
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    return@Runnable
                }
                if (mCountAbort.get()) {
                    Log.d("myTag","cancel thread")
                    return@Runnable
                }
                screenTimeSeconds++
                Log.d("myTag", "screen time: " + screenTimeSeconds + "s")
            }
        }).start()
    }
}