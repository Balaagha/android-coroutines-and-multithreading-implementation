package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase3

import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.BaseViewModel
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.MockApi

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {

    }

    fun performNetworkRequestsConcurrently() {

    }
}