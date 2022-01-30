package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.structuredconcurrency

import kotlinx.coroutines.*

fun main() {

    val exceptionHandler = CoroutineExceptionHandler { context, exception ->
        println("Caught exception $exception")
    }

    // If we write Job() instead of SupervisorJob() when Coroutine 1 fail, Coroutine 2 also cancelled
    val scope = CoroutineScope(SupervisorJob() + exceptionHandler)

    scope.launch {
        println("Coroutine 1 starts")
        delay(50)
        println("Coroutine 1 fails")
        throw RuntimeException()
    }

    scope.launch {
        println("Coroutine 2 starts")
        delay(500)
        println("Coroutine 2 completed")
    }.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine 2 got cancelled!")
        }
    }

    Thread.sleep(1000)

    println("Scope got cancelled: ${!scope.isActive}")

}