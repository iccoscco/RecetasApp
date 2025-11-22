package com.example.recetasapp.data.model

// Representa una categoría (como "Postres", "Carnes", "Bebidas"), con su id, nombre, imagen y descripción.
data class Category(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String
)
