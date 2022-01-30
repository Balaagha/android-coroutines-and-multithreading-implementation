package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.cancellation

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {

//    normalUseCaseWithoutCancel(this)
//    useCasWithEnsureActiveForCancellation(this)
//    useCasWithYieldForCancellation(this)
//    useCasWithIsActivePropertyForCancellation(this)
    useCasWithIsActivePropertyForCancellationWithNonCancellableLine(this)
//    val globalCoroutineJob = GlobalScope.launch {
//        repeat(10) {
//            println("$it")
//            delay(100)
//        }
//    }
//    delay(250)
//    globalCoroutineJob.cancel()
//    delay(1000)
}

suspend fun normalUseCaseWithoutCancel(scope: CoroutineScope){
    val job = scope.launch(Dispatchers.Default) {
        repeat(10) { index ->
            println("operation number: $index")
            Thread.sleep(100)
        }
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()
}

suspend fun useCasWithEnsureActiveForCancellation(scope: CoroutineScope){
    val job = scope.launch(Dispatchers.Default) {
        repeat(10) { index ->
            ensureActive()
            println("operation number: $index")
            Thread.sleep(100)
        }
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()
}

// Purpose of using yield is to give other coroutines a chance to run.
suspend fun useCasWithYieldForCancellation(scope: CoroutineScope){
    val job = scope.launch(Dispatchers.Default) {
        repeat(10) { index ->
            yield()
            println("operation number: $index")
            Thread.sleep(100)
        }
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()
}

// The benefit of taking the executive property compared to ensureActive() or yield() is that it doesn't immediately through a cancellation exception, so we can perform, for instance, some clean up operation before the coroutine finally shuts down.
suspend fun useCasWithIsActivePropertyForCancellation(scope: CoroutineScope){
    val job = scope.launch(Dispatchers.Default) {
        repeat(10) { index ->
            if(this.isActive){
                println("operation number: $index")
                Thread.sleep(100)
            } else {
//                delay(1000)  // delay not working in there. Because coroutine scope has been cancelled
                println("Cleaning up ... ")
                throw CancellationException()
            }
        }
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()
}

suspend fun useCasWithIsActivePropertyForCancellationWithNonCancellableLine(scope: CoroutineScope){
    val job = scope.launch(Dispatchers.Default) {
        repeat(10) { index ->
            if(this.isActive){
                println("operation number: $index")
                Thread.sleep(100)
            } else {
                withContext(NonCancellable){
                    delay(1000)
                    println("Cleaning up ... ")
                    throw CancellationException()
                }
            }
        }
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()
}