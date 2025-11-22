package com.example.recetasapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recetasapp.R
import com.example.recetasapp.utils.PreferencesHelper
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var preferencesHelper: PreferencesHelper

    private lateinit var darkModeSwitch: SwitchMaterial

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesHelper = PreferencesHelper(requireContext())
        initViewModel()
        initViews(view)
        setupClickListeners(view)
        loadDarkModePreference()
    }

    private fun initViewModel() {
        val factory = SettingsViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]
    }

    private fun initViews(view: View) {
        darkModeSwitch = view.findViewById(R.id.dark_mode_switch)
    }

    private fun setupClickListeners(view: View) {
        // Editar perfil
        val editProfileOption = view.findViewById<TextView>(R.id.edit_profile_option)
        editProfileOption.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_editProfile)
        }

        // Modo oscuro - NO configurar listener aquí, se hace en loadDarkModePreference()

        // Notificaciones
        val notificationsOption = view.findViewById<TextView>(R.id.notifications_option)
        notificationsOption.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_notifications)
        }

        // Idioma
        val languageOption = view.findViewById<TextView>(R.id.language_option)
        languageOption.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_language)
        }

        // Acerca de
        val aboutOption = view.findViewById<TextView>(R.id.about_option)
        aboutOption.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_about)
        }
    }

    private fun loadDarkModePreference() {
        val isDarkMode = preferencesHelper.isDarkModeEnabled()
        // Remover listener temporalmente para evitar loop
        darkModeSwitch.setOnCheckedChangeListener(null)
        darkModeSwitch.isChecked = isDarkMode
        // Volver a agregar listener
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            setDarkMode(isChecked)
        }
    }

    private fun setDarkMode(enabled: Boolean) {
        // Guardar preferencia
        preferencesHelper.setDarkMode(enabled)

        // Aplicar modo oscuro
        val mode = if (enabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        // Cambiar modo (esto recrea la Activity)
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}