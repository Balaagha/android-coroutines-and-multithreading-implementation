package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase1

import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.BaseViewModel
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.MockApi

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {

    }
}