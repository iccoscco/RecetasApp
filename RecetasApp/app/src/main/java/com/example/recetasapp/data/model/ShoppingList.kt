package com.example.recetasapp.data.model

// Representa una lista de compras completa,
data class ShoppingList(
    val id: String,
    val items: List<ShoppingItem>,
    val createdDate: Long = System.currentTimeMillis()
)

// Representa un ítem dentro de la lista de compras(nombre del ingrediente, cantidad, etc)
data class ShoppingItem(
    val ingredient: String,
    val quantity: String,
    val category: String,
    var isChecked: Boolean = false
)
