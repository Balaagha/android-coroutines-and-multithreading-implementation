package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase1

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.BaseViewModel
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.MockApi
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            Log.d("myTag","thread name is ${Thread.currentThread().name} and id is ${Thread.currentThread().id}}")

            try {
                val recentAndroidVersions = mockApi.getRecentAndroidVersions()
                uiState.value = UiState.Success(recentAndroidVersions)
            }catch (e: Exception){
                uiState.value = UiState.Error("Network request failed!")
            }
        }
    }
}