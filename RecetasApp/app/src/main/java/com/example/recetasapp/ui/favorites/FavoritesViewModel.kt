package com.example.recetasapp.ui.favorites

import androidx.lifecycle.*
import com.example.recetasapp.data.model.Recipe
import com.example.recetasapp.utils.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _favoriteRecipes = MutableLiveData<List<Recipe>>()
    val favoriteRecipes: LiveData<List<Recipe>> = _favoriteRecipes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /*
    init {
        loadFavorites()
    }
    */
    /*
    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            // run IO work on Dispatchers.IO
            val favorites = withContext(Dispatchers.IO) {
                preferencesHelper.getFavorites()
            }
            _favoriteRecipes.value = favorites
            _isLoading.value = false
        }
    }
    */
    /*
    fun removeFavorite(recipe: Recipe) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesHelper.removeFavorite(recipe.id)
            }
            loadFavorites()
        }
    }
    */

}

class FavoritesViewModelFactory(
    private val preferencesHelper: PreferencesHelper
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(preferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
