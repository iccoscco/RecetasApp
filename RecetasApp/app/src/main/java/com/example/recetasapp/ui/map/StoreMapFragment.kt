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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.recetasapp.R
import com.example.recetasapp.databinding.FragmentStoreMapBinding
import com.example.recetasapp.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class StoreMapFragment : Fragment() {

    // Cambiamos a ViewBinding para facilitar el acceso a las vistas
    private var _binding: FragmentStoreMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: StoreMapViewModel
    private lateinit var mapView: MapView

    // Para la BottomSheet
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    // Para la ruta
    private var roadOverlay: Polyline? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoreMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews()
        setupMap()
        setupObservers()
    }

    private fun initViewModel() {
        val factory = StoreMapViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[StoreMapViewModel::class.java]
    }

    private fun initViews() {
        mapView = binding.mapView // Referencia al MapView

        binding.searchEditTextMap.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchStores(s.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        // Botton Sheet
        bottomSheetBehavior = BottomSheetBehavior.from(binding.storeInfoBottomSheet.root)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // Empieza oculta

        // Listener para el botón "Cómo llegar" dentro de la BottomSheet
        binding.storeInfoBottomSheet.getDirectionsButton.setOnClickListener {
            viewModel.getDirections()
        }

        // borrar la ruta si el usuario cierra la bottom sheet
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    viewModel.clearSelectedStore()
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
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
        // Lista para mantener una referencia a todos los marcadores de tiendas
        val storeMarkers = mutableListOf<Marker>()

        viewModel.filteredStores.observe(viewLifecycleOwner, object : Observer<List<Store>> {
            override fun onChanged(stores: List<Store>) {
                // se ejecutará la primera vez que lleguen los datos.
                mapView.overlays.removeAll(storeMarkers)
                storeMarkers.clear()

                // Crea todos los marcadores y guárdalos
                stores.forEach { store ->
                    val storeMarker = Marker(mapView)
                    storeMarker.position = GeoPoint(store.latitude, store.longitude)
                    storeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    storeMarker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_map)
                    storeMarker.title = store.name

                    // Asocia la tienda con el marcador para poder identificarla luego
                    storeMarker.relatedObject = store

                    storeMarker.setOnMarkerClickListener { marker, _ ->
                        viewModel.selectStore(store)
                        mapView.controller.animateTo(marker.position)
                        true
                    }
                    storeMarkers.add(storeMarker) // Añade el marcador a nuestra lista local
                }

                // Añade todos los nuevos marcadores al mapa
                mapView.overlays.addAll(storeMarkers)

                // Redibuja el mapa
                mapView.invalidate()
                viewModel.filteredStores.removeObserver(this)

                setupSearchObserver(storeMarkers)
            }
        })


        // Observador para la tienda seleccionada
        viewModel.selectedStore.observe(viewLifecycleOwner) { store ->
            if (store != null) {
                binding.storeInfoBottomSheet.storeNameText.text = store.name
                binding.storeInfoBottomSheet.storeDistanceText.text = "Distancia: ${store.distance} km"
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        // Observador para la ruta
        viewModel.route.observe(viewLifecycleOwner) { road ->
            if (roadOverlay != null) {
                mapView.overlays.remove(roadOverlay)
                roadOverlay = null // Limpia la referencia
            }

            if (road != null && road.mStatus == Road.STATUS_OK) {
                roadOverlay = RoadManager.buildRoadOverlay(road)
                mapView.overlays.add(roadOverlay)
                mapView.invalidate()
            } else if (road != null) {
                requireContext().showToast("Error al calcular la ruta: ${road.mStatus}")
            }
        }

        // Observador para el estado de carga de la ruta
        viewModel.isLoadingRoute.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.storeInfoBottomSheet.getDirectionsButton.text = "Calculando..."
                binding.storeInfoBottomSheet.getDirectionsButton.isEnabled = false
            } else {
                binding.storeInfoBottomSheet.getDirectionsButton.text = "Cómo llegar"
                binding.storeInfoBottomSheet.getDirectionsButton.isEnabled = true
            }
        }
    }

    private fun setupSearchObserver(storeMarkers: List<Marker>) {
        viewModel.filteredStores.observe(viewLifecycleOwner) { filteredStores ->
            // Obtenemos los IDs de las tiendas filtradas para una búsqueda rápida
            val filteredStoreIds = filteredStores.map { it.id }.toSet()

            storeMarkers.forEach { marker ->
                val store = marker.relatedObject as Store
                marker.isEnabled = store.id in filteredStoreIds
            }

            mapView.overlays.removeAll { it is Marker && it.title == "Tu ubicación" }
            addUserLocationMarker(viewModel.userLocation.value)
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
        storeMarker.setOnMarkerClickListener { marker, _ ->
            viewModel.selectStore(store) // Llama al ViewModel para seleccionar la tienda
            mapView.controller.animateTo(marker.position) // Centra el mapa en el marcador
            true
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
        _binding = null
    }
}