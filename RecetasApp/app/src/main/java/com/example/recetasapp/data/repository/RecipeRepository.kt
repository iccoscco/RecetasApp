package com.example.recetasapp.data.repository

import com.example.recetasapp.data.model.Category
import com.example.recetasapp.data.model.Ingredient
import com.example.recetasapp.data.model.Recipe
import com.example.recetasapp.data.remote.api.MealDbApi
import com.example.recetasapp.data.remote.dto.MealDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Trae las recetas desde la API TheMealDB
class RecipeRepository(private val api: MealDbApi) {

    // Busca recetas por nombre
    suspend fun searchRecipes(query: String): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val response = api.searchMealByName(query)
            response.meals?.map { it.toRecipe() } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Obtiene una sola receta por su ID
    suspend fun getRecipeById(id: String): Recipe? = withContext(Dispatchers.IO) {
        try {
            val response = api.getMealById(id)
            response.meals?.firstOrNull()?.toRecipe()
        } catch (e: Exception) {
            null
        }
    }

    // Pide recetas aleatorias de la API
    suspend fun getRandomRecipes(count: Int = 10): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val recipes = mutableListOf<Recipe>()
            repeat(count) {
                val response = api.getRandomMeal()
                response.meals?.firstOrNull()?.let { recipes.add(it.toRecipe()) }
            }
            recipes
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Pide todas las categorías de comida (pasta, carne, postres, etc.).
    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        try {
            val response = api.getCategories()
            response.categories?.map { 
                Category(
                    id = it.idCategory,
                    name = it.strCategory,
                    imageUrl = it.strCategoryThumb,
                    description = it.strCategoryDescription
                )
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Devuelve recetas por categoría en base a su ID
    suspend fun getRecipesByCategory(category: String): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val response = api.filterByCategory(category)
            response.meals?.mapNotNull { 
                getRecipeById(it.idMeal)
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Devuelve recetas de acuerdo a la Area(Peru, México, etc)
    suspend fun getRecipesByArea(area: String): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val response = api.filterByArea(area)
            response.meals?.mapNotNull { 
                getRecipeById(it.idMeal)
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Mapper de DTO a Model
    private fun MealDto.toRecipe(): Recipe {
        val ingredients = mutableListOf<Ingredient>()
        
        // Extraer ingredientes y cantidades
        val ingredientsList = listOf(
            strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
            strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
            strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
            strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20
        )
        
        val measuresList = listOf(
            strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
            strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
            strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
            strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20
        )

        // Verificar las listas obtenidas
        for (i in ingredientsList.indices) {
            val ingredient = ingredientsList[i]
            val measure = measuresList[i]
            if (!ingredient.isNullOrBlank() && ingredient.trim().isNotEmpty()) {
                ingredients.add(
                    Ingredient(
                        name = ingredient.trim(),
                        quantity = measure?.trim() ?: ""
                    )
                )
            }
        }
        
        return Recipe(
            id = idMeal,
            name = strMeal,
            category = strCategory ?: "Sin categoría",
            area = strArea ?: "Internacional",
            instructions = strInstructions ?: "",
            imageUrl = strMealThumb ?: "",
            videoUrl = strYoutube,
            ingredients = ingredients,
            tags = strTags?.split(",")?.map { it.trim() } ?: emptyList(),
            preparationTime = "30 min", // TheMealDB no provee esto, valor por defecto
            difficulty = "Media",
            calories = null
        )
    }
}
