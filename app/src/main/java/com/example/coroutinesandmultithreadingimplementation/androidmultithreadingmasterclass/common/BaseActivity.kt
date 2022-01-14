package com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.common

import androidx.appcompat.app.AppCompatActivity
import com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.common.di.PresentationCompositionRoot

open class BaseActivity: AppCompatActivity() {

    protected val compositionRoot by lazy {
        PresentationCompositionRoot(this)
    }

}