package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase2.callbacks

import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.BaseViewModel
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.AndroidVersion
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading
        val getAndroidVersionsCall = mockApi.getRecentAndroidVersions()
        getAndroidVersionsCall.enqueue(object : Callback<List<AndroidVersion>>{
            override fun onResponse(
                call: Call<List<AndroidVersion>>,
                response: Response<List<AndroidVersion>>,
            ) {
                if (response.isSuccessful){
                    val mostResentVersion = response.body()?.last()
                    mostResentVersion?.apiLevel?.let { apiLevelValue ->
                        val getAndroidFeaturesCall = mockApi.getAndroidVersionFeatures(apiLevelValue)
                        getAndroidFeaturesCall.enqueue(object : Callback<VersionFeatures> {
                            override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                                uiState.value = UiState.Error("Network Request failed")
                            }

                            override fun onResponse(
                                call: Call<VersionFeatures>,
                                response: Response<VersionFeatures>
                            ) {
                                if (response.isSuccessful) {
                                    val featuresOfMostRecentVersion = response.body()!!
                                    uiState.value = UiState.Success(featuresOfMostRecentVersion)
                                } else {
                                    uiState.value = UiState.Error("Network Request failed")
                                }
                            }
                        })
                    }
                } else {
                    uiState.value = UiState.Error("Network request failed!")
                }
            }

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                uiState.value = UiState.Error("Something unexpected happened!")
            }

        })
    }
}