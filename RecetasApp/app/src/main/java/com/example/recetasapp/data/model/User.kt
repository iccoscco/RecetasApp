package com.example.recetasapp.data.model

// Representa un usuario de la app
data class User(
    val id: Int = 0,
    val email: String,
    val username: String,
    val password: String = "",
    val profileImageUrl: String? = null,
    val culinaryGoal: String? = null,
    val savedRecipes: Int = 0,
    val uploadedRecipes: Int = 0,
    val weeklyMenus: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)