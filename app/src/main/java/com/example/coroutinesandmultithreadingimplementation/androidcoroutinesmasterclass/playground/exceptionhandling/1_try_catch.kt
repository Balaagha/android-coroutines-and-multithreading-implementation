package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.exceptionhandling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main() {
    val exceptionHandler = CoroutineExceptionHandler { context, exception ->
        println("Caught $exception in CoroutineExceptionHandler")
    }

//    val scope = CoroutineScope(Job() + exceptionHandler)
    val scope = CoroutineScope(Job())

    try {
        scope.launch {
            functionThatThrowsIt()
        }
    } catch (e: Exception) {
        println("Caught: $e")
    }

    Thread.sleep(100)
}

fun functionThatThrowsIt() {
    throw RuntimeException()
}