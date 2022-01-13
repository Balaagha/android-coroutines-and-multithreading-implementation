package com.example.coroutinesandmultithreadingimplementation.home

enum class ScreenReachableFromHome( name: String) {

    PROBLEM_1("ANR Problem(More time action in UI Thread)"),
    PROBLEM_2("GC memory leaks problem"),
    UI_THREAD_DEMONSTRATION("UI Thread Demo"),
    UI_HANDLER_DEMONSTRATION("UI Handler Demo"),
    CUSTOM_HANDLER_DEMONSTRATION("Custom Handler Demo"),
    PROBLEM_3("Count in back thread and show it in Ui(with runnable)"),
    ATOMICITY_DEMONSTRATION("Atomicity Demo"),
    EXERCISE_4("Multithreading Problem(Factorial Calculation)"),
    EXERCISE_4_WITH_USE_CASE("Multithreading Problem(Factorial Calculation) with UseCase"),
    THREAD_WAIT_DEMONSTRATION("Thread Wait Demo"),
    EXERCISE_5("Receive messaging Problem"),
    DESIGN_WITH_THREADS_DEMONSTRATION("Design Demo: Threads"),
    DESIGN_WITH_THREAD_POOL_DEMONSTRATION("Design Demo: Thread Pool"),
    EXERCISE_7("Exercise 7"),
    DESIGN_WITH_ASYNCTASK_DEMONSTRATION("Design Demo: AsyncTask")
    ;


    private var mName: String? = null
    init{
        mName = name
    }

    open fun getName(): String? {
        return mName
    }

}
