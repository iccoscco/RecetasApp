package com.example.recetasapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetasapp.utils.PreferencesHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ViewModel de la pantalla Splash y su función principal es verificar si el usuario ya está logueado.
class SplashViewModel(private val preferencesHelper: PreferencesHelper) : ViewModel() {

    private val _navigateToAuth = MutableLiveData<Boolean>()
    val navigateToAuth: LiveData<Boolean> = _navigateToAuth

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean> = _navigateToMain

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            delay(2000) // Splash duration

            val user = preferencesHelper.getUser()
            if (user != null) {
                _navigateToMain.value = true
            } else {
                _navigateToAuth.value = true
            }
        }
    }
}