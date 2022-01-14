package com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.example

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coroutinesandmultithreadingimplementation.R
import kotlinx.android.synthetic.main.fragment_problem3.*

class Problem3Fragment : Fragment() {

    private val mUiHandler = Handler(Looper.getMainLooper())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_problem3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_count_seconds.setOnClickListener {
            countIterations()
        }
    }

    private fun countIterations() {
        btn_count_seconds.setEnabled(false)
        Thread(Runnable {
            for (i in 1..SECONDS_TO_COUNT) {
                mUiHandler.post(Runnable { txt_count.text = i.toString() })
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    return@Runnable
                }
            }
            mUiHandler.post(Runnable {
                txt_count.text = "Done!"
                btn_count_seconds.isEnabled = true
            })
        }).start()
    }

    companion object {
        private const val SECONDS_TO_COUNT = 3
    }
}