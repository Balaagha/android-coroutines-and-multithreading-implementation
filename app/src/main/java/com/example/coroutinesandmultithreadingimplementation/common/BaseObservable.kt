package com.example.coroutinesandmultithreadingimplementation.common

import android.util.Log
import java.util.*
import kotlin.collections.HashSet

abstract class BaseObservable<LISTENER_CLASS> {

    private val MONITOR = Object()
    private val mListeners: HashSet<LISTENER_CLASS> = HashSet()

    open fun registerListener(listener: LISTENER_CLASS) {
        Log.d("myTag","@registerListener  listener => $listener")
        synchronized(MONITOR) {
            val hadNoListeners = mListeners.size == 0
            mListeners.add(listener)
            if (hadNoListeners && mListeners.size == 1) {
                onFirstListenerRegistered()
            }
        }
    }

    open fun unregisterListener(listener: LISTENER_CLASS) {
        Log.d("myTag","@unregisterListener  listener => $listener")
        synchronized(MONITOR) {
            val hadOneListener = mListeners.size == 1
            mListeners.remove(listener)
            if (hadOneListener && mListeners.size == 0) {
                onLastListenerUnregistered()
            }
        }
    }

    protected open fun getListeners(): Set<LISTENER_CLASS> {
        synchronized(MONITOR) {
            Log.d("myTag","in the getListeners function")
            return Collections.unmodifiableSet(HashSet(mListeners))
        }
    }

    protected open fun onFirstListenerRegistered() {}

    protected open fun onLastListenerUnregistered() {}
}