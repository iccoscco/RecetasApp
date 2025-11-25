package com.example.recetasapp.ui.map

import androidx.lifecycle.*

data class Store(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Double
)

class StoreMapViewModel : ViewModel() {

    // Lista original con todas las tiendas
    private var allStores: List<Store> = emptyList()

    private val _filteredStores = MutableLiveData<List<Store>>()
    val filteredStores: LiveData<List<Store>> = _filteredStores

    // Ubicación del usuario
    private val _userLocation = MutableLiveData(Pair(-16.406452, -71.524666))
    val userLocation: LiveData<Pair<Double, Double>> = _userLocation

    init {
        loadNearbyStores()
    }

    fun loadNearbyStores() {
        allStores = listOf(
            Store("1", "Mercado San Camilo", -16.401918, -71.536767, 1.2),
            Store("2", "Supermercado Franco", -16.409395, -71.543477, 2.1),
            Store("3", "Tienda 'El Vecino'", -16.397576, -71.529881, 1.5),
            Store("4", "Plaza Vea Arequipa Center", -16.392931, -71.551790, 3.0),
            Store("5", "Tottus Parque Lambramani", -16.421715, -71.526543, 2.8)
        )
        _filteredStores.value = allStores
    }

    fun searchStores(query: String) {
        val filtered = if (query.isBlank()) {
            allStores
        } else {
            allStores.filter { store ->
                store.name.contains(query, ignoreCase = true)
            }
        }
        _filteredStores.value = filtered
    }

    fun updateUserLocation(latitude: Double, longitude: Double) {
        _userLocation.value = Pair(latitude, longitude)
        loadNearbyStores()
    }
}

class StoreMapViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreMapViewModel::class.java)) {
            return StoreMapViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}