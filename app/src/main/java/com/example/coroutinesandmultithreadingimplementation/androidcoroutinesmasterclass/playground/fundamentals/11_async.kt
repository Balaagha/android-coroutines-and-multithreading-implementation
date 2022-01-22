package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.fundamentals

import kotlinx.coroutines.*

fun main(){
    calculateWIthAsync()
}

fun calculateWithoutAsync(){
    runBlocking {
        launch(Dispatchers.IO) {
            println("Calculation started...")
            val time = System.currentTimeMillis()
            val stock1 = getStock1()
            val stock2 = getStock2()
            val total = stock1 + stock2
            println("total = $total | operation finished in ${System.currentTimeMillis() - time}")
        }
    }
}

fun calculateWIthAsync(){
    runBlocking {
        launch(Dispatchers.Main) {
            println("Calculation started...")
            val time = System.currentTimeMillis()
            val stock1 = async(Dispatchers.IO) { getStock1() }
            val stock2 = async(Dispatchers.IO) { getStock2() }
            val total = stock1.await() + stock2.await()
            println("total = $total | operation finished in ${System.currentTimeMillis() - time}")
        }
    }
}



private suspend fun getStock1(): Int{
    delay(3000)
    println("stock 1 returned")
    return 55000
}

private suspend fun getStock2(): Int{
    delay(4000)
    println("stock 2 returned")
    return 45000
}



