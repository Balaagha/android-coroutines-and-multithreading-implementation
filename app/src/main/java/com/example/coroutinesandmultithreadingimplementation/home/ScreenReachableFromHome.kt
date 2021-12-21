package com.example.coroutinesandmultithreadingimplementation.home

enum class ScreenReachableFromHome( name: String) {

    TEMP("Temp");
    private var mName: String? = null
    init{
        mName = name
    }

    open fun getName(): String? {
        return mName
    }

}
