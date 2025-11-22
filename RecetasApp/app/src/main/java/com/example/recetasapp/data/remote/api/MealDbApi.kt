package com.example.recetasapp.data.remote.api

import com.example.recetasapp.data.remote.dto.CategoryResponse
import com.example.recetasapp.data.remote.dto.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

// Endpoints para consumir la API
interface MealDbApi {
    
    @GET("search.php")
    suspend fun searchMealByName(
        @Query("s") name: String
    ): MealResponse
    
    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: String
    ): MealResponse
    
    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse
    
    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse
    
    @GET("filter.php")
    suspend fun filterByCategory(
        @Query("c") category: String
    ): MealResponse
    
    @GET("filter.php")
    suspend fun filterByArea(
        @Query("a") area: String
    ): MealResponse
    
    @GET("list.php")
    suspend fun listCategories(
        @Query("c") type: String = "list"
    ): CategoryResponse
    
    @GET("list.php")
    suspend fun listAreas(
        @Query("a") type: String = "list"
    ): CategoryResponse
    
    @GET("list.php")
    suspend fun listIngredients(
        @Query("i") type: String = "list"
    ): CategoryResponse
}
