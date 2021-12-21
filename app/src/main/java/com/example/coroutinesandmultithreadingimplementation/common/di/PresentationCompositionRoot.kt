package com.example.coroutinesandmultithreadingimplementation.common.di

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.coroutinesandmultithreadingimplementation.common.ToolbarManipulator
import com.techyourchance.fragmenthelper.FragmentContainerWrapper

class PresentationCompositionRoot(activity: FragmentActivity) {

    private var mActivity: FragmentActivity? = null

    init {
        this.mActivity = activity
    }


    fun getToolbarManipulator(): ToolbarManipulator {
        return mActivity as ToolbarManipulator
    }

}