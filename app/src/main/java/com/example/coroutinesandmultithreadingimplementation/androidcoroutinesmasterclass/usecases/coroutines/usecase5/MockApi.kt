package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase5

import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.createMockApi
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.mockAndroidVersions
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.utils.MockNetworkInterceptor
import com.google.gson.Gson

fun mockApi() = createMockApi(
    MockNetworkInterceptor()
        .mock(
            "http://localhost/recent-android-versions",
            Gson().toJson(mockAndroidVersions),
            200,
            1000
        )
)