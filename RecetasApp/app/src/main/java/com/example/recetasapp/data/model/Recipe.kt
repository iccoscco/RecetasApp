package com.example.recetasapp.data.model

// Representa una receta completa, con su nombre, categoría, origen, instrucciones, image, etc
data class Recipe(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val imageUrl: String,
    val videoUrl: String? = null,
    val ingredients: List<Ingredient>,
    val tags: List<String> = emptyList(),
    val preparationTime: String? = null,
    val difficulty: String = "Media",
    val calories: Int? = null,
    val proteins: String? = null,
    val carbohydrates: String? = null,
    val rating: Float = 0f,
    val isFavorite: Boolean = false,
    val author: String? = null // Autor de la receta (para comunidad)
)
