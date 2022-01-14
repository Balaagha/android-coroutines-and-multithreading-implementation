package com.example.coroutinesandmultithreadingimplementation.menu

enum class ScreenReachableFromMainMenu(name: String) {

    MULTITHREADING_VASILITY("Android Multithreading Masterclass (Vasility Zukanov)"),
    COROUTINES_VASILITY("Kotlin Coroutines for Android Masterclass (Vasility Zukanov)"),
    COROUTINES_LUKAS("Mastering Kotlin Coroutines for Android Development (Lukas Lechner)"),
    ;


    private var mName: String? = null
    init{
        mName = name
    }

    open fun getName(): String? {
        return mName
    }

}
