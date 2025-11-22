package com.example.recetasapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Representa el JSON que devuelve la API.(una sola categoría)
data class CategoryDto(
    @SerializedName("idCategory")
    val idCategory: String,
    
    @SerializedName("strCategory")
    val strCategory: String,
    
    @SerializedName("strCategoryThumb")
    val strCategoryThumb: String,
    
    @SerializedName("strCategoryDescription")
    val strCategoryDescription: String
)
