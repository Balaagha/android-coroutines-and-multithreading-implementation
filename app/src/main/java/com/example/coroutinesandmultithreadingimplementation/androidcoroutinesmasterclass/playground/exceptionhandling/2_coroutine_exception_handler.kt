package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.exceptionhandling

import kotlinx.coroutines.*

fun main() {

    val exceptionHandlerMain = CoroutineExceptionHandler { context, exception ->
        println("Caught $exception in CoroutineExceptionHandler main")
    }

    val exceptionHandlerInside = CoroutineExceptionHandler { context, exception ->
        println("Caught $exception in CoroutineExceptionHandler inside")
    }

    val scope = CoroutineScope(SupervisorJob())

    scope.launch {
        launch(exceptionHandlerInside) {
            functionThatThrowsIt()
        }
    }

    Thread.sleep(100)
}