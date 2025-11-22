package com.example.recetasapp.ui.cookingmode

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Lógica del Modo Cocina
class CookingModeViewModel : ViewModel() {

    private val _currentStepIndex = MutableLiveData(1)
    val currentStepIndex: LiveData<Int> = _currentStepIndex

    private val _totalSteps = MutableLiveData(5)
    val totalSteps: LiveData<Int> = _totalSteps

    private val _currentStep = MutableLiveData<String>()
    val currentStep: LiveData<String> = _currentStep

    private val _timerSeconds = MutableLiveData(0)
    val timerSeconds: LiveData<Int> = _timerSeconds

    private val steps = listOf(
        "Precalentar el horno a 180°C",
        "Mezclar los ingredientes secos en un bowl",
        "Agregar los ingredientes húmedos y mezclar bien",
        "Verter la mezcla en el molde preparado",
        "Hornear durante 30-35 minutos"
    )

    init {
        _totalSteps.value = steps.size
        loadCurrentStep()
        startTimer()
    }

    fun loadRecipe(recipeId: String) {
        // Cargar receta desde el repositorio y extraer pasos de las instrucciones
        _totalSteps.value = steps.size
        loadCurrentStep()
    }

    // Maneja el índice de los pasos de la receta
    private fun loadCurrentStep() {
        val index = _currentStepIndex.value ?: 1
        _currentStep.value = steps.getOrNull(index - 1) ?: ""
    }

    fun nextStep() {
        val current = _currentStepIndex.value ?: 1
        val total = _totalSteps.value ?: steps.size

        if (current < total) {
            _currentStepIndex.value = current + 1
            loadCurrentStep()
        }
    }

    fun previousStep() {
        val current = _currentStepIndex.value ?: 1

        if (current > 1) {
            _currentStepIndex.value = current - 1
            loadCurrentStep()
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _timerSeconds.value = (_timerSeconds.value ?: 0) + 1
            }
        }
    }
}

class CookingModeViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CookingModeViewModel::class.java)) {
            return CookingModeViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}