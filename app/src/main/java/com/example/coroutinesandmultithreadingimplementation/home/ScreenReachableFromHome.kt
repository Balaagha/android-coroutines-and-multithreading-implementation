package com.example.coroutinesandmultithreadingimplementation.home

enum class ScreenReachableFromHome( name: String) {

    PROBLEM_1("ANR Problem(More time action in UI Thread)"),
    PROBLEM_2("GC memory leaks problem"),
    UI_THREAD_DEMONSTRATION("UI Thread Demo"),
    UI_HANDLER_DEMONSTRATION("UI Handler Demo"),
    CUSTOM_HANDLER_DEMONSTRATION("Custom Handler Demo"),
    EXERCISE_3("Exercise 3"),
    ATOMICITY_DEMONSTRATION("Atomicity Demo"),
    EXERCISE_4("Exercise 4"),
    THREAD_WAIT_DEMONSTRATION("Thread Wait Demo"),
    EXERCISE_5("Exercise 5"),
    DESIGN_WITH_THREADS_DEMONSTRATION("Design Demo: Threads"),
    EXERCISE_6("Exercise 6"),
    DESIGN_WITH_THREAD_POOL_DEMONSTRATION("Design Demo: Thread Pool"),
    EXERCISE_7("Exercise 7") ;
    private var mName: String? = null
    init{
        mName = name
    }

    open fun getName(): String? {
        return mName
    }

}
