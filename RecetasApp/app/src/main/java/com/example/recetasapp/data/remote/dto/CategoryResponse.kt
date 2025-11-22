package com.example.recetasapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Representa la respuesta completa que devuelve la API.
data class CategoryResponse(
    @SerializedName("categories")
    val categories: List<CategoryDto>?
)