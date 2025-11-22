package com.example.recetasapp.ui.community

import androidx.lifecycle.*
import com.example.recetasapp.data.model.Recipe
import com.example.recetasapp.data.repository.RecipeRepository
import kotlinx.coroutines.launch

class CommunityViewModel(
    private val repository: RecipeRepository
) : ViewModel() {
    
    private val _communityRecipes = MutableLiveData<List<Recipe>>()
    val communityRecipes: LiveData<List<Recipe>> = _communityRecipes
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    init {
        loadCommunityRecipes()
    }
    
    fun loadCommunityRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getRandomRecipes(20)
                _communityRecipes.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar recetas"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun likeRecipe(recipeId: String) {
        // TODO: Implementar like
    }
    
    fun commentRecipe(recipeId: String, comment: String) {
        // TODO: Implementar comentarios
    }
}

class CommunityViewModelFactory(
    private val repository: RecipeRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            return CommunityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
