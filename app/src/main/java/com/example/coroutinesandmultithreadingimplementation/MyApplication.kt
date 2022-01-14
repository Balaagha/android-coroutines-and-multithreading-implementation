package com.example.coroutinesandmultithreadingimplementation

import android.app.Application
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        System.setProperty(IO_PARALLELISM_PROPERTY_NAME, Int.MAX_VALUE.toString())
    }

}
