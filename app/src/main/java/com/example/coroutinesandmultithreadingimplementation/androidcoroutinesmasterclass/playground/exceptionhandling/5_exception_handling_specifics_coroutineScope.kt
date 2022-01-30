package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() = runBlocking<Unit>() {

    try {
        launch {
            throw RuntimeException()
        }
    } catch (e: Exception) {
        println("Caught $e")
    }

//    try {
//        coroutineScope {
//            launch {
//                throw RuntimeException()
//            }
//        }
//    } catch (e: Exception) {
//        println("Caught $e")
//    }

//    try {
//        doSomeThingSuspend()
//    } catch (e: Exception) {
//        println("Caught $e")
//    }

}

private suspend fun doSomeThingSuspend() {
    coroutineScope {
        launch {
            throw RuntimeException()
        }
    }
}
