package com.example.recetasapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recetasapp.R
import com.example.recetasapp.ui.auth.AuthActivity
import com.example.recetasapp.utils.PreferencesHelper

// Obtiene la información del usuario desde el ProfileViewModel.
class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    private lateinit var userAvatar: ImageView
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var savedRecipesCount: TextView
    private lateinit var uploadedRecipesCount: TextView
    private lateinit var weeklyMenusCount: TextView
    private lateinit var editProfileButton: Button
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews(view)
        setupListeners()
        setupObservers()
    }

    private fun initViewModel() {
        val preferencesHelper = PreferencesHelper(requireContext())
        val factory = ProfileViewModelFactory(preferencesHelper)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    private fun initViews(view: View) {
        userAvatar = view.findViewById(R.id.user_avatar)
        userName = view.findViewById(R.id.user_name)
        userEmail = view.findViewById(R.id.user_email)
        savedRecipesCount = view.findViewById(R.id.saved_recipes_count)
        uploadedRecipesCount = view.findViewById(R.id.uploaded_recipes_count)
        weeklyMenusCount = view.findViewById(R.id.weekly_menus_count)
        editProfileButton = view.findViewById(R.id.edit_profile_button)
        logoutButton = view.findViewById(R.id.logout_button)
    }

    private fun setupListeners() {
        editProfileButton.setOnClickListener {
            // Navegar a editar perfil
            findNavController().navigate(R.id.action_profile_to_editProfile)
        }

        logoutButton.setOnClickListener {
            viewModel.logout()
            navigateToAuth()
        }
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                userName.text = it.username
                userEmail.text = it.email
            }
        }

        viewModel.savedRecipes.observe(viewLifecycleOwner) { count ->
            savedRecipesCount.text = count.toString()
        }

        viewModel.uploadedRecipes.observe(viewLifecycleOwner) { count ->
            uploadedRecipesCount.text = count.toString()
        }

        viewModel.weeklyMenus.observe(viewLifecycleOwner) { count ->
            weeklyMenusCount.text = count.toString()
        }
    }

    // Se limpia el historial y se abre la pantalla de login.
    private fun navigateToAuth() {
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}