package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase2.rx

import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.BaseViewModel

class SequentialNetworkRequestsRxViewModel(
    private val mockApi: RxMockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {

    }
}