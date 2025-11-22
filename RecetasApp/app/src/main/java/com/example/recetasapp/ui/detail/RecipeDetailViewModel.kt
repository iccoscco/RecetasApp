package com.example.recetasapp.ui.detail

import androidx.lifecycle.*
import com.example.recetasapp.data.model.Recipe
import com.example.recetasapp.data.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeDetailViewModel(private val repository: RecipeRepository) : ViewModel() {
    
    private val _recipe = MutableLiveData<Recipe?>()
    val recipe: LiveData<Recipe?> = _recipe
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun loadRecipe(recipeId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getRecipeById(recipeId)
                _recipe.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar receta"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun toggleFavorite() {
        _recipe.value?.let { recipe ->
            _recipe.value = recipe.copy(isFavorite = !recipe.isFavorite)
            // TODO: Guardar en PreferencesHelper
        }
    }
}

class RecipeDetailViewModelFactory(
    private val repository: RecipeRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            return RecipeDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
