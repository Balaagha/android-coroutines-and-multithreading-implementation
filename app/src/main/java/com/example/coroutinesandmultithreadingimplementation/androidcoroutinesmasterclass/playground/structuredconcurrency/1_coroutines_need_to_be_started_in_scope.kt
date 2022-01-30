package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.structuredconcurrency

import kotlinx.coroutines.*

val scope = CoroutineScope(Dispatchers.Default)
const val jobOperationTime = 100L
const val mainFunctionLifeCycleTime = 50L

fun main() = runBlocking<Unit> {


    val job = scope.launch {
        delay(jobOperationTime)
        println("Coroutine completed")
    }

    job.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine was cancelled")
        } else {
            println("Coroutine was finished successfully")
        }
    }

    delay(mainFunctionLifeCycleTime)
    onDestroy()
}

fun onDestroy() {
    println("life-time of scope ends")
    scope.cancel()
}