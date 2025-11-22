package com.example.recetasapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recetasapp.data.model.Category
import com.example.recetasapp.data.model.Recipe
import com.example.recetasapp.data.repository.RecipeRepository
import kotlinx.coroutines.launch

// ViewModel de la pantalla principal
class HomeViewModel(private val repository: RecipeRepository) : ViewModel() {
    
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes
    
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    init {
        loadData()
    }
    
    private fun loadData() {
        loadRecommendedRecipes()
        loadCategories()
    }

    // Carga recetas recomendadas
    fun loadRecommendedRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getRandomRecipes(10)
                _recipes.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar recetas"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Cargar categorías de recetas
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val result = repository.getCategories()
                _categories.value = result
            } catch (e: Exception) {
                // Silenciar errores de categorías si las recetas ya cargaron
            }
        }
    }
    // Buscar recetas por nombre
    fun searchRecipes(query: String) {
        if (query.isBlank()) {
            loadRecommendedRecipes()
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.searchRecipes(query)
                _recipes.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Error en la búsqueda"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Cargar recetas por categoría
    fun loadRecipesByCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getRecipesByCategory(category)
                _recipes.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar categoría"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun retry() {
        loadRecommendedRecipes()
    }
}

// Permite crear instancias de HomeViewModel
class HomeViewModelFactory(
    private val repository: RecipeRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
