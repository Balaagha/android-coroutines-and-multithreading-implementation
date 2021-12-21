package com.example.coroutinesandmultithreadingimplementation.common

import androidx.fragment.app.Fragment
import com.example.coroutinesandmultithreadingimplementation.common.di.PresentationCompositionRoot

open class BaseFragment: Fragment() {

    protected val compositionRoot by lazy {
        PresentationCompositionRoot(requireActivity())
    }

}