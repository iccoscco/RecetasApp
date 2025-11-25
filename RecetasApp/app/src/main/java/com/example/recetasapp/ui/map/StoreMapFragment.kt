package com.example.recetasapp.ui.map

import android.os.Bundle
import android.text.TextWatcher
import android.text.Editable
import android.widget.EditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recetasapp.R
import com.example.recetasapp.utils.showToast
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class StoreMapFragment : Fragment() {

    private lateinit var viewModel: StoreMapViewModel
    private lateinit var mapView: MapView
    private lateinit var searchEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews(view)
        setupMap()
        setupObservers()
    }

    private fun initViewModel() {
        val factory = StoreMapViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[StoreMapViewModel::class.java]
    }

    private fun initViews(view: View) {
        mapView = view.findViewById(R.id.map_view) // Referencia al MapView
        searchEditText = view.findViewById(R.id.search_edit_text_map)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchStores(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true) // zoom con dos dedos

        // Centrar el mapa en la ubicación inicial del usuario
        val userLocationPoint = viewModel.userLocation.value
        if (userLocationPoint != null) {
            val startPoint = GeoPoint(userLocationPoint.first, userLocationPoint.second)
            mapView.controller.setCenter(startPoint)
            mapView.controller.setZoom(15.0) // Nivel de zoom inicial
        }
        addUserLocationMarker(userLocationPoint)
    }

    private fun setupObservers() {
        viewModel.filteredStores.observe(viewLifecycleOwner) { stores ->
            mapView.overlays.clear() // Limpiar marcadores antiguos
            addUserLocationMarker(viewModel.userLocation.value)
            stores.forEach { store ->
                addStoreMarker(store)
            }
            mapView.invalidate()
        }
    }

    private fun addUserLocationMarker(location: Pair<Double, Double>?) {
        if (location == null) return

        val userMarker = Marker(mapView)
        userMarker.position = GeoPoint(location.first, location.second)
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        userMarker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_boy)
        userMarker.title = "Tu ubicación"
        mapView.overlays.add(userMarker)
    }

    private fun addStoreMarker(store: Store) {
        val storeMarker = Marker(mapView)
        storeMarker.position = GeoPoint(store.latitude, store.longitude)
        storeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        storeMarker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_map)
        storeMarker.title = store.name // El título que aparece al tocar el marcador

        // Listener para cuando el usuario toca el marcador
        storeMarker.setOnMarkerClickListener { marker, mapView ->
            marker.showInfoWindow() // Muestra el título
            requireContext().showToast("Tienda seleccionada: ${store.name}")
            // TODO: Aquí irá la lógica para mostrar opciones como "Obtener ruta"
            true // Indica que hemos manejado el evento
        }

        mapView.overlays.add(storeMarker)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume() // Necesario para que el mapa se cargue correctamente
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause() // Necesario para pausar el renderizado del mapa
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpia el mapa para liberar recursos
        mapView.onDetach()
    }
}