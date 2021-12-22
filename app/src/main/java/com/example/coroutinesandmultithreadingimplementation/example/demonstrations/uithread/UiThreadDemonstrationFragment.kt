package com.example.coroutinesandmultithreadingimplementation.example.demonstrations.uithread

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coroutinesandmultithreadingimplementation.R
import com.example.coroutinesandmultithreadingimplementation.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_ui_thread_demonstration.*


class UiThreadDemonstrationFragment : BaseFragment() {
    private val TAG = "UiThreadDemonstration"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logThreadInfo("onCreate()")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ui_thread_demonstration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositionRoot.getToolbarManipulator().setScreenTitle("UI thread Demonstration")
        btn_callback_check.setOnClickListener {
            logThreadInfo("button callback")
        }
        logThreadInfo("onViewCreated()")
    }

    override fun onStart() {
        super.onStart()
        logThreadInfo("onStart()")
    }

    override fun onResume() {
        super.onResume()
        logThreadInfo("onResume()")
    }

    override fun onPause() {
        super.onPause()
        logThreadInfo("onPause()")
    }

    override fun onStop() {
        super.onStop()
        logThreadInfo("onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logThreadInfo("onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        logThreadInfo("onDestroy()")
    }


    private fun logThreadInfo(eventName: String) {
        Log.d(TAG, "event => $eventName | thread name: ${Thread.currentThread().name} | thread id: ${Thread.currentThread().id}")
    }

}