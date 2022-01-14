package com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.example.demonstrations.customhandler

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coroutinesandmultithreadingimplementation.R
import kotlinx.android.synthetic.main.fragment_custom_handler_demonstration.*


class CustomHandlerDemonstrationFragment : Fragment() {

    private var mCustomHandler: CustomHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_handler_demonstration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_stop_job.setOnClickListener {
            mCustomHandler?.stop()
        }
        btn_send_job.setOnClickListener {
            sendJob()
        }
    }
    private fun sendJob() {
        mCustomHandler?.post(Runnable {
            for (i in 0 until SECONDS_TO_COUNT) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    return@Runnable
                }
                Log.d("CustomHandler", "iteration: $i")
            }
        })
    }

    override fun onStart() {
        super.onStart()
        mCustomHandler = CustomHandler()
    }

    override fun onStop() {
        super.onStop()
        mCustomHandler!!.stop()
    }

    companion object {
        private const val SECONDS_TO_COUNT = 5;
    }
}