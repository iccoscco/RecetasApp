package com.example.recetasapp.ui.weeklyplan

import androidx.lifecycle.*
import com.example.recetasapp.data.model.Recipe

class WeeklyPlanViewModel : ViewModel() {

    private val _weeklyMeals = MutableLiveData<List<Recipe>>()
    val weeklyMeals: LiveData<List<Recipe>> = _weeklyMeals

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    init {
        loadWeeklyPlan()
    }

    fun loadWeeklyPlan() {
        // TODO: Cargar plan semanal
        _weeklyMeals.value = emptyList()
    }

    fun generateShoppingList() {
        _message.value = "Lista de compras generada"
    }
}

class WeeklyPlanViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeeklyPlanViewModel::class.java)) {
            return WeeklyPlanViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}