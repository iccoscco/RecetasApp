package com.example.recetasapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recetasapp.R
import com.example.recetasapp.utils.PreferencesHelper
import com.google.android.material.switchmaterial.SwitchMaterial

class NotificationsFragment : Fragment() {

    private lateinit var preferencesHelper: PreferencesHelper

    private lateinit var generalSwitch: SwitchMaterial
    private lateinit var recipeRemindersSwitch: SwitchMaterial
    private lateinit var newRecipesSwitch: SwitchMaterial
    private lateinit var cookingModeSwitch: SwitchMaterial

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesHelper = PreferencesHelper(requireContext())
        initViews(view)
        setupListeners()
    }

    private fun initViews(view: View) {
        generalSwitch = view.findViewById(R.id.general_notifications_switch)
        recipeRemindersSwitch = view.findViewById(R.id.recipe_reminders_switch)
        newRecipesSwitch = view.findViewById(R.id.new_recipes_switch)
        cookingModeSwitch = view.findViewById(R.id.cooking_mode_switch)
    }

    private fun setupListeners() {
        generalSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Guardar preferencia
        }

        recipeRemindersSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Guardar preferencia
        }

        newRecipesSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Guardar preferencia
        }

        cookingModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Guardar preferencia
        }
    }
}