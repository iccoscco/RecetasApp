package com.example.recetasapp.data.model

//Representa un ingrediente de una receta, con su nombre, cantidad y si el usuario ya lo marcó como usado.
data class Ingredient(
    val name: String,
    val quantity: String,
    var isChecked: Boolean = false
)
