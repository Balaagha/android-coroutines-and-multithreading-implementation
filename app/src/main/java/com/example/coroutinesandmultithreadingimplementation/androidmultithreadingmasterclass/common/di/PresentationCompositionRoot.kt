package com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.common.di

import androidx.fragment.app.FragmentActivity
import com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.common.ToolbarManipulator

class PresentationCompositionRoot(activity: FragmentActivity) {

    private var mActivity: FragmentActivity? = null

    init {
        this.mActivity = activity
    }


    fun getToolbarManipulator(): ToolbarManipulator {
        return mActivity as ToolbarManipulator
    }

}