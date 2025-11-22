package com.example.recetasapp.ui.profile

import androidx.lifecycle.*
import com.example.recetasapp.data.local.database.AppDatabase
import com.example.recetasapp.data.local.entities.UserEntity
import com.example.recetasapp.utils.PreferencesHelper
import kotlinx.coroutines.launch

// Carga de datos de ususario y validación
class EditProfileViewModel(
    private val database: AppDatabase,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _user = MutableLiveData<UserEntity?>()
    val user: LiveData<UserEntity> = _user as LiveData<UserEntity>

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadCurrentUser()
    }

    // Obtiene el userId desde PreferencesHelper
    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val userId = preferencesHelper.getUserId()
                if (userId != -1) {
                    val currentUser = database.userDao().getUserById(userId)
                    _user.value = currentUser
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar usuario: ${e.message}"
            }
        }
    }

    // Lógica para actualizar datos y validar cosas.
    fun updateProfile(
        newUsername: String,
        newEmail: String,
        currentPassword: String?,
        newPassword: String?,
        confirmPassword: String?
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val currentUser = _user.value
                if (currentUser == null) {
                    _errorMessage.value = "Usuario no encontrado"
                    _isLoading.value = false
                    return@launch
                }

                // Validaciones
                if (newUsername.isBlank()) {
                    _errorMessage.value = "El nombre de usuario no puede estar vacío"
                    _isLoading.value = false
                    return@launch
                }

                if (newEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    _errorMessage.value = "Email inválido"
                    _isLoading.value = false
                    return@launch
                }

                // Si quiere cambiar contraseña
                var finalPassword = currentUser.password
                if (!currentPassword.isNullOrBlank() || !newPassword.isNullOrBlank()) {
                    if (currentPassword != currentUser.password) {
                        _errorMessage.value = "La contraseña actual es incorrecta"
                        _isLoading.value = false
                        return@launch
                    }

                    if (newPassword.isNullOrBlank() || newPassword.length < 6) {
                        _errorMessage.value = "La nueva contraseña debe tener al menos 6 caracteres"
                        _isLoading.value = false
                        return@launch
                    }

                    if (newPassword != confirmPassword) {
                        _errorMessage.value = "Las contraseñas no coinciden"
                        _isLoading.value = false
                        return@launch
                    }

                    finalPassword = newPassword
                }

                // Verificar si el email ya existe (si lo cambió)
                if (newEmail != currentUser.email) {
                    val existingUser = database.userDao().getUserByEmail(newEmail)
                    if (existingUser != null && existingUser.id != currentUser.id) {
                        _errorMessage.value = "Este email ya está registrado"
                        _isLoading.value = false
                        return@launch
                    }
                }

                // Actualizar usuario
                val updatedUser = currentUser.copy(
                    username = newUsername,
                    email = newEmail,
                    password = finalPassword,
                    updatedAt = System.currentTimeMillis()
                )

                database.userDao().updateUser(updatedUser)

                // Actualizar en preferencias si cambió el email
                if (newEmail != currentUser.email) {
                    preferencesHelper.saveUserEmail(newEmail)
                }

                _user.value = updatedUser
                _updateSuccess.value = true
                _errorMessage.value = null

            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar: ${e.message}"
                _updateSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val currentUser = _user.value
                if (currentUser != null) {
                    // Eliminar de la base de datos
                    database.userDao().deleteUser(currentUser)

                    // Limpiar preferencias
                    preferencesHelper.clearUserData()

                    _updateSuccess.value = true
                } else {
                    _errorMessage.value = "Usuario no encontrado"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar cuenta: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Clase necesaria porque el ViewModel recibe dos dependencias externas
class EditProfileViewModelFactory(
    private val database: AppDatabase,
    private val preferencesHelper: PreferencesHelper
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(database, preferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}