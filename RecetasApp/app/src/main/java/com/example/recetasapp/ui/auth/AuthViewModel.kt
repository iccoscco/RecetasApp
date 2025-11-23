package com.example.recetasapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recetasapp.data.model.User
import com.example.recetasapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

// Maneja la lógica de Autenticación
class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    
    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult
    
    private val _registerResult = MutableLiveData<Result<User>>()
    val registerResult: LiveData<Result<User>> = _registerResult
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Logea un usuario
    fun login(email: String, password: String) {
        viewModelScope.launch { // Corutina
            _isLoading.value = true
            val result = repository.login(email, password)
            _loginResult.value = result
            _isLoading.value = false
        }
    }

    // Registra un usuario
    fun register(email: String, password: String, username: String) {
        viewModelScope.launch { // corutina
            _isLoading.value = true
            val result = repository.register(email, password, username)
            _registerResult.value = result
            _isLoading.value = false
        }
    }

    fun loginWithGoogle(idToken: String, nonce: String) {
        // Kami no nani mani oose no mama ni
        viewModelScope.launch { // corutina
            _isLoading.value = true
            val result = repository.loginWithGoogle(idToken, nonce)
            _loginResult.value = result
            _isLoading.value = false
        }
    }
    
    // TODO: Implementar login con Facebook
    fun loginWithFacebook() {
        // Las librerías de Kotlin son más molestas que las de Node
        viewModelScope.launch { // Corutina
            _isLoading.value = true
            val result = repository.loginWithFacebook()
            _loginResult.value = result
            _isLoading.value = false
        }
    }
}

class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
