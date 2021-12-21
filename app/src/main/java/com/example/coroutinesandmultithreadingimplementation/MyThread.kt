package com.example.coroutinesandmultithreadingimplementation

class MyThread: Thread() {
    private var mSeed: Int = 0
    fun myThread(seed: Int){ mSeed = seed }
    override fun run() {
        super.run()
        // perform some calculation
    }
}