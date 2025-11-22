package com.example.recetasapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Respuesta completa que devuelve la API cuando pides una o varias recetas.
data class MealResponse(
    @SerializedName("meals")
    val meals: List<MealDto>?
)