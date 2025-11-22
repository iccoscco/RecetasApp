package com.example.recetasapp.ui.settings

import androidx.lifecycle.*

class SettingsViewModel : ViewModel() {

    private val _notificationsEnabled = MutableLiveData(true)
    val notificationsEnabled: LiveData<Boolean> = _notificationsEnabled

    private val _darkModeEnabled = MutableLiveData(false)
    val darkModeEnabled: LiveData<Boolean> = _darkModeEnabled

    private val _language = MutableLiveData("es")
    val language: LiveData<String> = _language

    fun toggleNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        // TODO: Guardar en PreferencesHelper
    }

    fun toggleDarkMode(enabled: Boolean) {
        _darkModeEnabled.value = enabled
        // TODO: Aplicar tema
    }

    fun changeLanguage(languageCode: String) {
        _language.value = languageCode
        // TODO: Cambiar idioma de la app
    }
}

class SettingsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}