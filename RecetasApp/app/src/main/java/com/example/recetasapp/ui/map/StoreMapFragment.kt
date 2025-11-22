package com.example.recetasapp.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recetasapp.R
import com.example.recetasapp.utils.showToast

class StoreMapFragment : Fragment() {

    private lateinit var viewModel: StoreMapViewModel
    private lateinit var mapContainer: FrameLayout

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
        setupObservers()

        // TODO: Inicializar Google Maps
        requireContext().showToast("Mapa próximamente (Google Maps API)")
    }

    private fun initViewModel() {
        val factory = StoreMapViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[StoreMapViewModel::class.java]
    }

    private fun initViews(view: View) {
        mapContainer = view.findViewById(R.id.map_container)
    }

    private fun setupObservers() {
        viewModel.nearbyStores.observe(viewLifecycleOwner) { stores ->
            // TODO: Mostrar marcadores en el mapa
        }
    }
}