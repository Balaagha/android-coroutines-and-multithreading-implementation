package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.structuredconcurrency

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {

    val scope = CoroutineScope(Dispatchers.Default)

    scope.coroutineContext[Job]!!.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("@invokeOnCompletion -> Parent(scope) job was cancelled")
        } else {
            println("@invokeOnCompletion -> Parent(scope) job not cancelled")
        }
    }

    val childCoroutine1Job = scope.launch {
        delay(1000)
        println("Coroutine 1 completed")
    }
    childCoroutine1Job.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine 1 was cancelled!")
        } else {
            println("Coroutine 1 was not cancelled!")
        }
    }


    val childCoroutine2Job = scope.launch {
        delay(1000)
        println("Coroutine 2 completed")
    }
    childCoroutine2Job.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine 2 was cancelled!")
        } else {
            println("Coroutine 2 was not cancelled!")
        }
    }

    delay(200)
    childCoroutine1Job.cancelAndJoin()
//    scope.coroutineContext[Job]!!.cancelAndJoin()

}