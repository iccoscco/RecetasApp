package com.example.recetasapp.ui.map

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.util.GeoPoint
import java.text.DecimalFormat

data class Store(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Double
)

class StoreMapViewModel(application: Application) : AndroidViewModel(application) {

    // Lista original con todas las tiendas
    private var allStores: List<Store> = emptyList()

    private val _filteredStores = MutableLiveData<List<Store>>()
    val filteredStores: LiveData<List<Store>> = _filteredStores

    // Ubicación del usuario
    private val _userLocation = MutableLiveData(Pair(-16.406452, -71.524666))
    val userLocation: LiveData<Pair<Double, Double>> = _userLocation

    // Rutas
    private val _selectedStore = MutableLiveData<Store?>()
    val selectedStore: LiveData<Store?> = _selectedStore

    private val _route = MutableLiveData<Road?>()
    val route: LiveData<Road?> = _route

    private val _isLoadingRoute = MutableLiveData<Boolean>()
    val isLoadingRoute: LiveData<Boolean> = _isLoadingRoute

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

    fun selectStore(store: Store) {
        // Calcular la distancia
        val userLoc = userLocation.value ?: return
        val distance = calculateDistance(userLoc.first, userLoc.second, store.latitude, store.longitude)

        val df = DecimalFormat("#.##")
        val formattedDistance = df.format(distance).toDouble()

        _selectedStore.value = store.copy(distance = formattedDistance)
    }

    fun clearSelectedStore() {
        _selectedStore.value = null
        _route.value = null
    }

    fun getDirections() {
        val store = _selectedStore.value ?: return
        val userLoc = userLocation.value ?: return

        _isLoadingRoute.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val roadManager = OSRMRoadManager(getApplication(), "RecetasApp/1.0") // User-Agent
            val waypoints = ArrayList<GeoPoint>()
            waypoints.add(GeoPoint(userLoc.first, userLoc.second))
            waypoints.add(GeoPoint(store.latitude, store.longitude))

            try {
                val road = roadManager.getRoad(waypoints)
                _route.postValue(road)
            } catch (e: Exception) {
                // Manejar error de red o de cálculo de ruta
                _route.postValue(null)
                e.printStackTrace()
            } finally {
                _isLoadingRoute.postValue(false)
            }
        }
    }

    // Función para calcular la distancia en KM (Fórmula de Haversine)
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // Radio de la Tierra en km
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

    fun updateUserLocation(latitude: Double, longitude: Double) {
        _userLocation.value = Pair(latitude, longitude)
        loadNearbyStores()
    }

}

class StoreMapViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreMapViewModel::class.java)) {
            return StoreMapViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}