package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase1

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.BaseActivity
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.useCase1Description
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.utils.fromHtml
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.utils.setGone
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.utils.setVisible
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.utils.toast
import com.example.coroutinesandmultithreadingimplementation.databinding.ActivityPerformsinglenetworkrequestBinding

class PerformSingleNetworkRequestActivity : BaseActivity() {

    override fun getToolbarTitle() = useCase1Description

    private val binding by lazy { ActivityPerformsinglenetworkrequestBinding.inflate(layoutInflater) }
    private val viewModel: PerformSingleNetworkRequestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        binding.btnPerformSingleNetworkRequest.setOnClickListener {
            viewModel.performSingleNetworkRequest()
        }
    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> {
                onLoad()
            }
            is UiState.Success -> {
                onSuccess(uiState)
            }
            is UiState.Error -> {
                onError(uiState)
            }
        }
    }

    private fun onLoad() = with(binding) {
        progressBar.setVisible()
        textViewResult.text = ""
        btnPerformSingleNetworkRequest.isEnabled = false
    }

    private fun onSuccess(uiState: UiState.Success) = with(binding) {
        progressBar.setGone()
        btnPerformSingleNetworkRequest.isEnabled = true
        val readableVersions = uiState.recentVersions.map { "API ${it.apiLevel}: ${it.name}" }
        textViewResult.text = fromHtml(
            "<b>Recent Android Versions</b><br>${readableVersions.joinToString(separator = "<br>")}"
        )
    }

    private fun onError(uiState: UiState.Error) = with(binding) {
        progressBar.setGone()
        btnPerformSingleNetworkRequest.isEnabled = true
        toast(uiState.message)
    }
}