package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase3

import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.createMockApi
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.mockVersionFeaturesAndroid10
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.mockVersionFeaturesOreo
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.mockVersionFeaturesPie
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.utils.MockNetworkInterceptor
import com.google.gson.Gson

fun mockApi() = createMockApi(
    MockNetworkInterceptor()
        .mock(
            "http://localhost/android-version-features/27",
            Gson().toJson(mockVersionFeaturesOreo),
            200,
            1000
        )
        .mock(
            "http://localhost/android-version-features/28",
            Gson().toJson(mockVersionFeaturesPie),
            200,
            1000
        )
        .mock(
            "http://localhost/android-version-features/29",
            Gson().toJson(mockVersionFeaturesAndroid10),
            200,
            1000
        )
)