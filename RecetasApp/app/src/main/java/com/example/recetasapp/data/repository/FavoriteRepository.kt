package com.example.recetasapp.data.repository

import com.example.recetasapp.data.model.Recipe
import com.example.recetasapp.utils.PreferencesHelper

// Maneja las recetas favoritas
class FavoriteRepository(private val preferencesHelper: PreferencesHelper) {

    // Devuelve la lista completa de recetas favoritas
    fun getFavorites(): List<String> {
        return preferencesHelper.getFavorites() //TODO: REVISAR
    }

    // Agregar un Receta como favorita
    fun addFavorite(recipe: Recipe): Boolean { //TODO: REVISAR
        return try {
            preferencesHelper.addFavorite(recipe.toString())
            true
        } catch (e: Exception) {
            false
        }
    }

    // Remover una receta de favorita con su ID
    fun removeFavorite(recipeId: String): Boolean {
        return try {
            preferencesHelper.removeFavorite(recipeId)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Comprobar si una receta es favorita
    fun isFavorite(recipeId: String): Boolean {
        return preferencesHelper.isFavorite(recipeId)
    }

    // Limpiar todas las recetas favoritas
    fun clearFavorites(): Boolean {
        return try {
            preferencesHelper.clearFavorites()
            true
        } catch (e: Exception) {
            false
        }
    }


}
