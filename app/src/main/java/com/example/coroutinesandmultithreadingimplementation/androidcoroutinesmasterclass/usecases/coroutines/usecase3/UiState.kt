package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase3

import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.VersionFeatures

sealed class UiState {
    object Loading : UiState()
    data class Success(
        val versionFeatures: List<VersionFeatures>
    ) : UiState()

    data class Error(val message: String) : UiState()
}