package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.playground.fundamentals

import kotlin.concurrent.thread

fun main() {
    repeat(1000) {
        thread {
            Thread.sleep(5000)
            print(".")
        }
    }
}