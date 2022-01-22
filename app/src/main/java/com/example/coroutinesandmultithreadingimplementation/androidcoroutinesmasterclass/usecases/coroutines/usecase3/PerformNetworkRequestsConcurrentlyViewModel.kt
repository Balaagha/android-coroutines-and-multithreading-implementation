package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.BaseViewModel
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.Exception

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val oreoFeatures = mockApi.getAndroidVersionFeatures(27)
                val pieFeatures = mockApi.getAndroidVersionFeatures(28)
                val android10Features = mockApi.getAndroidVersionFeatures(29)

                val versionFeatures = listOf(oreoFeatures,pieFeatures,android10Features)

                uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception){
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading

        val oreoFeaturesDeferred = viewModelScope.async { mockApi.getAndroidVersionFeatures(27) }
        val pieFeaturesDeferred = viewModelScope.async { mockApi.getAndroidVersionFeatures(28) }
        val android10FeaturesDeferred = viewModelScope.async { mockApi.getAndroidVersionFeatures(29) }


        viewModelScope.launch {
            try {
//                val versionFeatures = listOf(oreoFeaturesDeferred.await(), pieFeaturesDeferred.await(), android10FeaturesDeferred.await())
                val versionFeatures = awaitAll(oreoFeaturesDeferred, pieFeaturesDeferred, android10FeaturesDeferred)
                uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception){
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }
}