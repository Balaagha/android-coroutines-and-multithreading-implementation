package com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.common

import androidx.fragment.app.Fragment
import com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.common.di.PresentationCompositionRoot

open class BaseFragment: Fragment() {

    protected val compositionRoot by lazy {
        PresentationCompositionRoot(requireActivity())
    }

}