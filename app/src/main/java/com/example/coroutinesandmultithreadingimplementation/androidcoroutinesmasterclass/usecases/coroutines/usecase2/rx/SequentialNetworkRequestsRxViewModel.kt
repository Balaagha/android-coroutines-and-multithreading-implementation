package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase2.rx

import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers.io

class SequentialNetworkRequestsRxViewModel(
    private val mockApi: RxMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val disposables = CompositeDisposable()

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading
        mockApi.getRecentAndroidVersions().flatMap { androidVersions ->
            val recentVersion = androidVersions.last()
            mockApi.getAndroidVersionFeatures(recentVersion.apiLevel)
        }
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { featureVersions ->
                    uiState.value = UiState.Success(featureVersions)
                },
                onError = {
                    uiState.value = UiState.Error("Network Request failed.")
                }
            )
            .addTo(disposables)

    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}