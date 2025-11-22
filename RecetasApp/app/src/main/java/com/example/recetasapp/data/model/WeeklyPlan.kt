package com.example.recetasapp.data.model

// Plan semanal de comidas
data class WeeklyPlan(
    val weekStartDate: Long,
    val meals: Map<String, DayMeals>
)

// Comida al día
data class DayMeals(
    val dayName: String,
    val breakfast: Recipe? = null,
    val lunch: Recipe? = null,
    val dinner: Recipe? = null
)
