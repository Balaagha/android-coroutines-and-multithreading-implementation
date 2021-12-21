package com.example.coroutinesandmultithreadingimplementation.common

import androidx.appcompat.app.AppCompatActivity
import com.example.coroutinesandmultithreadingimplementation.common.di.PresentationCompositionRoot

open class BaseActivity: AppCompatActivity() {

    protected val compositionRoot by lazy {
        PresentationCompositionRoot(this)
    }

}