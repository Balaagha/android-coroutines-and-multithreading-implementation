package com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.example.demonstrations.atomicity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coroutinesandmultithreadingimplementation.R
import kotlinx.android.synthetic.main.fragment_atomicity_demonstration.*
import java.util.concurrent.atomic.AtomicInteger


class AtomicityDemonstrationFragment : Fragment() {

    private val mUiHandler = Handler(Looper.getMainLooper())

//    @Volatile
//    private var mCount: Int = 0

    @Volatile
    private var mCount: AtomicInteger = AtomicInteger(0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_atomicity_demonstration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_start_count.setOnClickListener { startCount() }

    }

    private fun startCount() {
//        mCount = 0
        mCount.set(0)
        txt_final_count.text = ""
        btn_start_count.isEnabled = false

        for (i in 0 until NUM_OF_COUNTER_THREADS) {
            startCountThread()
        }

        mUiHandler.postDelayed({
//            txt_final_count.text = mCount.toString()
            txt_final_count.text = mCount.get().toString()
            btn_start_count.isEnabled = true
        }, (NUM_OF_COUNTER_THREADS * 20).toLong())

    }

    private fun startCountThread() {
        Thread {
            for (i in 0 until COUNT_UP_TO) {
//                mCount++
                mCount.incrementAndGet()
            }
        }.start()
    }

    companion object {
        private val COUNT_UP_TO = 1000
        private val NUM_OF_COUNTER_THREADS = 100
    }

}