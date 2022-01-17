package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase2.callbacks

import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.BaseViewModel

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {

    }
}