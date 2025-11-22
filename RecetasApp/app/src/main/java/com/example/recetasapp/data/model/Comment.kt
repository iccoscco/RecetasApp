package com.example.recetasapp.data.model

// Representa un comentario de una receta
data class Comment(
    val id: String,
    val userId: Int,
    val userName: String,
    val userAvatar: String? = null,
    val recipeId: String,
    val text: String,
    val rating: Float,
    val createdAt: Long = System.currentTimeMillis()
)
