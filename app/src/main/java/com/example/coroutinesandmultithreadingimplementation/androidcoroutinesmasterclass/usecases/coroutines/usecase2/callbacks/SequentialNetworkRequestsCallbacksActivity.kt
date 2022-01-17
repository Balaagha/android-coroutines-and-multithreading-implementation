package com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.usecases.coroutines.usecase2.callbacks

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.BaseActivity
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.base.useCase2Description
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.utils.fromHtml
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.utils.setGone
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.utils.setVisible
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.utils.toast
import com.example.coroutinesandmultithreadingimplementation.databinding.ActivityPerform2sequentialnetworkrequestsBinding

class SequentialNetworkRequestsCallbacksActivity : BaseActivity() {

    private val binding by lazy {
        ActivityPerform2sequentialnetworkrequestsBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: SequentialNetworkRequestsCallbacksViewModel by viewModels()

    override fun getToolbarTitle() = useCase2Description

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        binding.btnRequestsSequentially.setOnClickListener {
            viewModel.perform2SequentialNetworkRequest()
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
    }

    private fun onSuccess(uiState: UiState.Success) = with(binding) {
        progressBar.setGone()
        textViewResult.text = fromHtml(
            "<b>Features of most recent Android Version \" ${uiState.versionFeatures.androidVersion.name} \"</b><br>" +
                    uiState.versionFeatures.features.joinToString(
                        prefix = "- ",
                        separator = "<br>- "
                    )
        )
    }

    private fun onError(uiState: UiState.Error) = with(binding) {
        progressBar.setGone()
        btnRequestsSequentially.isEnabled = true
        toast(uiState.message)
    }
}