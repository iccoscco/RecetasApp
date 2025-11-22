package com.example.recetasapp.ui.shoppinglist

import androidx.lifecycle.*
import com.example.recetasapp.data.model.Ingredient
import com.example.recetasapp.utils.PreferencesHelper

class ShoppingListViewModel(
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _shoppingItems = MutableLiveData<List<Ingredient>>()
    val shoppingItems: LiveData<List<Ingredient>> = _shoppingItems

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    init {
        loadShoppingList()
    }

    fun loadShoppingList() {
        // TODO: Cargar desde PreferencesHelper o Room
        _shoppingItems.value = emptyList()
    }

    fun updateItemStatus(item: Ingredient, isChecked: Boolean) {
        item.isChecked = isChecked
        _shoppingItems.value = _shoppingItems.value
    }

    fun deleteItem(item: Ingredient) {
        val currentList = _shoppingItems.value?.toMutableList() ?: mutableListOf()
        currentList.remove(item)
        _shoppingItems.value = currentList
        _message.value = "Ingrediente eliminado"
    }

    fun exportList() {
        _message.value = "Exportando lista..."
        // TODO: Implementar exportación
    }

    fun clearList() {
        _shoppingItems.value = emptyList()
        _message.value = "Lista limpiada"
    }
}

class ShoppingListViewModelFactory(
    private val preferencesHelper: PreferencesHelper
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            return ShoppingListViewModel(preferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}