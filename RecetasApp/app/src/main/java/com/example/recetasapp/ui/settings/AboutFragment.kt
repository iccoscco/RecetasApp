package com.example.recetasapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recetasapp.R
import com.example.recetasapp.utils.showToast
import com.google.android.material.button.MaterialButton

class AboutFragment : Fragment() {

    private lateinit var rateAppButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupListeners()
    }

    private fun initViews(view: View) {
        rateAppButton = view.findViewById(R.id.rate_app_button)
    }

    private fun setupListeners() {
        rateAppButton.setOnClickListener {
            requireContext().showToast("¡Gracias por tu apoyo!")
            // TODO: Abrir Google Play Store para calificar
        }
    }
}