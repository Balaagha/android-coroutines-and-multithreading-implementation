package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.exceptionhandling

import kotlinx.coroutines.*

fun main() {

    val exceptionHandler = CoroutineExceptionHandler { context, exception ->
        println("Caught $exception in CoroutineExceptionHandler")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

//    scope.async {
    scope.launch {
        val deferred = async {
            delay(200)
            throw RuntimeException()
        }
//        deferred.await()
    }

    Thread.sleep(1000)

}