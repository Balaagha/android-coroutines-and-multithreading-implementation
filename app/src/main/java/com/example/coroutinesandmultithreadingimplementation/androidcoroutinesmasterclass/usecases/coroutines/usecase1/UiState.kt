package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase1

import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.mock.AndroidVersion

sealed class UiState {
    object Loading : UiState()
    data class Success(val recentVersions: List<AndroidVersion>) : UiState()
    data class Error(val message: String) : UiState()
}