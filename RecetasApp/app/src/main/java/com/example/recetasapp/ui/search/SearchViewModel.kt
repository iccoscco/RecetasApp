package com.example.recetasapp.ui.search

import androidx.lifecycle.*
import com.example.recetasapp.data.model.Recipe
import com.example.recetasapp.data.repository.RecipeRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: RecipeRepository) : ViewModel() {
    
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun searchRecipes(query: String) {
        if (query.isBlank()) {
            _recipes.value = emptyList()
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
    
    fun filterByCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getRecipesByCategory(category)
                _recipes.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class SearchViewModelFactory(
    private val repository: RecipeRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
