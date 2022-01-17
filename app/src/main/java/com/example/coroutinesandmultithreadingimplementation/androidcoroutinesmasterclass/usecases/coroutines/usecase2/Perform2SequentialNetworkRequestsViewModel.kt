package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase2

import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.BaseViewModel
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.MockApi

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {

    }
}