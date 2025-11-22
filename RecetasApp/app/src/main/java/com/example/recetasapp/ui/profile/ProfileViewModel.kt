package com.example.recetasapp.ui.profile

import androidx.lifecycle.*
import com.example.recetasapp.data.model.User
import com.example.recetasapp.utils.PreferencesHelper
import kotlinx.coroutines.launch

// Lógica del perfil de usuario
class ProfileViewModel(
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {
    
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user
    
    private val _savedRecipes = MutableLiveData<Int>(0)
    val savedRecipes: LiveData<Int> = _savedRecipes
    
    private val _uploadedRecipes = MutableLiveData<Int>(0)
    val uploadedRecipes: LiveData<Int> = _uploadedRecipes
    
    private val _weeklyMenus = MutableLiveData<Int>(0)
    val weeklyMenus: LiveData<Int> = _weeklyMenus
    
    init {
        loadUserProfile()
        loadStatistics()
    }
    
    private fun loadUserProfile() {
        _user.value = preferencesHelper.getUser()
    }
    
    private fun loadStatistics() {
        _savedRecipes.value = preferencesHelper.getFavorites().size
        _uploadedRecipes.value = 0
        _weeklyMenus.value = 0
    }
    
    fun logout() {
        viewModelScope.launch {
            preferencesHelper.logout()
        }
    }
}

class ProfileViewModelFactory(
    private val preferencesHelper: PreferencesHelper
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(preferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
