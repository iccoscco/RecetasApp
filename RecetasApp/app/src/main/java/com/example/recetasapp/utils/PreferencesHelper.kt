package com.example.recetasapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.recetasapp.data.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesHelper(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        Constants.PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val gson = Gson()

    fun saveUser(user: User) {
        prefs.edit().apply {
            putInt(Constants.KEY_USER_ID, user.id)
            putString(Constants.KEY_USER_EMAIL, user.email)
            putString(Constants.KEY_USER_NAME, user.username)
            putBoolean(Constants.KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUser(): User? {
        val userId = prefs.getInt(Constants.KEY_USER_ID, -1)
        if (userId == -1) return null

        return User(
            id = userId,
            email = prefs.getString(Constants.KEY_USER_EMAIL, "") ?: "",
            username = prefs.getString(Constants.KEY_USER_NAME, "") ?: ""
        )
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun clearUserData() {
        logout() // Usa el mismo método para limpiar todo
    }

    fun saveFavorites(favorites: List<String>) {
        val json = gson.toJson(favorites)
        prefs.edit().putString(Constants.KEY_FAVORITES, json).apply()
    }

    fun getFavorites(): List<String> {
        val json = prefs.getString(Constants.KEY_FAVORITES, null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addFavorite(recipeId: String) {
        val favorites = getFavorites().toMutableList()
        if (!favorites.contains(recipeId)) {
            favorites.add(recipeId)
            saveFavorites(favorites)
        }
    }

    fun removeFavorite(recipeId: String) {
        val favorites = getFavorites().toMutableList()
        favorites.remove(recipeId)
        saveFavorites(favorites)
    }

    fun isFavorite(recipeId: String): Boolean {
        return getFavorites().contains(recipeId)
    }

    fun clearFavorites() {
        prefs.edit().remove(Constants.KEY_FAVORITES).apply()
    }

    fun getUserId(): Int {
        return prefs.getInt(Constants.KEY_USER_ID, -1)
    }

    fun saveUserEmail(email: String) {
        prefs.edit().putString(Constants.KEY_USER_EMAIL, email).apply()
    }

    // Modo Oscuro
    fun isDarkModeEnabled(): Boolean {
        return prefs.getBoolean(Constants.KEY_DARK_MODE, false)
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(Constants.KEY_DARK_MODE, enabled).apply()
    }
}