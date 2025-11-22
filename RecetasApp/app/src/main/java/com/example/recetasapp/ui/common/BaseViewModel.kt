package com.example.recetasapp.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    protected val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    protected val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    protected val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    protected fun showLoading() {
        _isLoading.value = true
    }

    protected fun hideLoading() {
        _isLoading.value = false
    }

    protected fun showError(error: String) {
        _error.value = error
        hideLoading()
    }

    protected fun showMessage(message: String) {
        _message.value = message
    }
}