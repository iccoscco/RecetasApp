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

    private val _nearbyStores = MutableLiveData<List<Store>>()
    val nearbyStores: LiveData<List<Store>> = _nearbyStores

    private val _userLocation = MutableLiveData<Pair<Double, Double>>()
    val userLocation: LiveData<Pair<Double, Double>> = _userLocation

    init {
        loadNearbyStores()
    }

    fun loadNearbyStores() {
        // TODO: Cargar tiendas cercanas usando Google Places API
        _nearbyStores.value = emptyList()
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