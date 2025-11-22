package com.example.recetasapp.utils

object Constants {

    // API
    const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    const val API_KEY = "1" // Free tier

    // Database
    const val DATABASE_NAME = "cocina_total_db"

    // SharedPreferences
    const val PREFS_NAME = "cocina_total_prefs"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_USER_NAME = "user_name"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_FAVORITES = "favorites"
    const val KEY_DARK_MODE = "dark_mode"

    // Notification Channels
    const val CHANNEL_ID_COOKING = "cooking_mode_channel"
    const val CHANNEL_ID_REMINDERS = "reminders_channel"
    const val CHANNEL_NAME_COOKING = "Modo Cocina"
    const val CHANNEL_NAME_REMINDERS = "Recordatorios"

    // Notification IDs
    const val NOTIFICATION_ID_COOKING = 1001
    const val NOTIFICATION_ID_REMINDER = 1002

    // Request Codes
    const val REQUEST_CODE_LOCATION = 100
    const val REQUEST_CODE_CAMERA = 101
    const val REQUEST_CODE_GALLERY = 102

    // Recipe Difficulty
    const val DIFFICULTY_EASY = "Fácil"
    const val DIFFICULTY_MEDIUM = "Media"
    const val DIFFICULTY_HARD = "Difícil"

    // Meal Times
    const val MEAL_BREAKFAST = "Desayuno"
    const val MEAL_LUNCH = "Almuerzo"
    const val MEAL_DINNER = "Cena"

    // Shopping List Categories
    val SHOPPING_CATEGORIES = listOf(
        "Frutas",
        "Verduras",
        "Carnes",
        "Pescados y Mariscos",
        "Lácteos",
        "Panadería",
        "Abarrotes",
        "Condimentos y Especias",
        "Bebidas",
        "Otros"
    )

    // Days of Week
    val DAYS_OF_WEEK = listOf(
        "Lunes",
        "Martes",
        "Miércoles",
        "Jueves",
        "Viernes",
        "Sábado",
        "Domingo"
    )
}