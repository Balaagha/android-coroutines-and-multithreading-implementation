package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.structuredconcurrency

import kotlinx.coroutines.*

/**
 * Coroutines started in the same scope form a hieararchy.
 *
 */
fun main() {

    val scopeJob = Job()
    val scope = CoroutineScope(Dispatchers.Default + scopeJob)

    val passedJob = Job()
    // Don't do like this. If we remove passedJob, [scopeJob.children.contains(coroutineJob)] return true as expected.
    val coroutineJob = scope.launch(passedJob) {
        println("Starting coroutine")
        delay(1000)
    }

    println("passedJob and coroutineJob are references to the same job object: ${passedJob === coroutineJob}")

    println("Is coroutineJob a child of scopeJob? =>${scopeJob.children.contains(coroutineJob)}")
}