package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.fundamentals

import kotlinx.coroutines.*

fun main(){
    runBlocking {
        launch(Dispatchers.IO) {
            val totalUserCount = getTotalUserCount()
            println("totalUserCount => $totalUserCount")
        }
    }
}

private suspend fun getTotalUserCount(): Int {
    var count = 0
    CoroutineScope(Dispatchers.IO).launch {
        delay(1000)
        count = 50
    }
    val deferred = CoroutineScope(Dispatchers.IO).async {
        delay(3000)
        return@async 70
    }
    return count + deferred.await()
}


