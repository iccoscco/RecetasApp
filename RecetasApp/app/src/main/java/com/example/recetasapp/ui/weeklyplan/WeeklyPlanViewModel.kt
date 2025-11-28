package com.example.recetasapp.ui.weeklyplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recetasapp.data.model.WeeklyPlan
import com.example.recetasapp.data.model.Recipe

class WeeklyPlanViewModel : ViewModel() {
    private val _plans = MutableLiveData<List<WeeklyPlan>>(emptyList())
    val plans: LiveData<List<WeeklyPlan>> = _plans

    private val _recipes = MutableLiveData<List<Recipe>>(emptyList())
    val recipes: LiveData<List<Recipe>> = _recipes

    fun addWeeklyPlan(plan: WeeklyPlan) {
        _plans.value = _plans.value?.plus(plan)
    }

    fun removeWeeklyPlan(plan: WeeklyPlan) {
        _plans.value = _plans.value?.filterNot { it == plan }
    }

    // Puedes poblar recetas de prueba aquí
    fun setTestRecipes() {
        val recipes = listOf(
            Recipe("1", "Desayuno 1", "Desayuno", "Local", "Instrucciones...", "", ingredients = emptyList()),
            Recipe("2", "Almuerzo 1", "Almuerzo", "Local", "Instrucciones...", "", ingredients = emptyList()),
            Recipe("3", "Cena 1", "Cena", "Local", "Instrucciones...", "", ingredients = emptyList()),
            Recipe("4", "Desayuno 2", "Desayuno", "Local", "Instrucciones...", "", ingredients = emptyList()),
            Recipe("5", "Almuerzo 2", "Almuerzo", "Local", "Instrucciones...", "", ingredients = emptyList()),
            Recipe("6", "Cena 2", "Cena", "Local", "Instrucciones...", "", ingredients = emptyList())
        )
        _recipes.value = recipes
    }
}