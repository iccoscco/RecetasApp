package com.example.recetasapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recetasapp.R
import com.example.recetasapp.data.local.database.AppDatabase
import com.example.recetasapp.data.local.database.DatabaseBuilder
import com.example.recetasapp.utils.PreferencesHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.snackbar.Snackbar
import android.content.Intent
import com.example.recetasapp.ui.auth.AuthActivity

// Fragment para editar perfil y eliminar la cuenta,
class EditProfileFragment : Fragment() {

    private lateinit var viewModel: EditProfileViewModel

    private lateinit var userAvatar: ImageView
    private lateinit var changePhotoText: TextView
    private lateinit var usernameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var currentPasswordEditText: TextInputEditText
    private lateinit var newPasswordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var saveButton: MaterialButton
    private lateinit var cancelButton: MaterialButton
    private lateinit var deleteAccountButton: MaterialButton
    private lateinit var loadingOverlay: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews(view)
        setupListeners()
        setupObservers()
    }

    private fun initViewModel() {
        val database = DatabaseBuilder.getInstance(requireContext())
        val preferencesHelper = PreferencesHelper(requireContext())
        val factory = EditProfileViewModelFactory(database, preferencesHelper)
        viewModel = ViewModelProvider(this, factory)[EditProfileViewModel::class.java]
    }

    private fun initViews(view: View) {
        userAvatar = view.findViewById(R.id.user_avatar)
        changePhotoText = view.findViewById(R.id.change_photo_text)
        usernameEditText = view.findViewById(R.id.username_edit_text)
        emailEditText = view.findViewById(R.id.email_edit_text)
        currentPasswordEditText = view.findViewById(R.id.current_password_edit_text)
        newPasswordEditText = view.findViewById(R.id.new_password_edit_text)
        confirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text)
        saveButton = view.findViewById(R.id.save_button)
        cancelButton = view.findViewById(R.id.cancel_button)
        deleteAccountButton = view.findViewById(R.id.delete_account_button)
        loadingOverlay = view.findViewById(R.id.loading_overlay)
    }

    private fun setupListeners() {
        changePhotoText.setOnClickListener {
            // TODO: Implementar cambio de foto
            Snackbar.make(requireView(), "Cambio de foto...", Snackbar.LENGTH_SHORT).show()
        }

        saveButton.setOnClickListener {
            saveChanges()
        }

        cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        deleteAccountButton.setOnClickListener {
            showDeleteAccountDialog()
        }
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                usernameEditText.setText(it.username)
                emailEditText.setText(it.email)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
            saveButton.isEnabled = !isLoading
            cancelButton.isEnabled = !isLoading
        }

        viewModel.updateSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Verificar si es una eliminación de cuenta
                if (viewModel.user.value == null) {
                    // Cuenta eliminada, redirigir a login
                    Snackbar.make(requireView(), "Cuenta eliminada correctamente", Snackbar.LENGTH_SHORT).show()
                    navigateToAuth()
                } else {
                    // Actualización normal
                    Snackbar.make(requireView(), "Perfil actualizado correctamente", Snackbar.LENGTH_LONG).show()

                    // Limpiar campos de contraseña
                    currentPasswordEditText.text?.clear()
                    newPasswordEditText.text?.clear()
                    confirmPasswordEditText.text?.clear()

                    // Volver atrás después de 1 segundo
                    requireView().postDelayed({
                        findNavController().navigateUp()
                    }, 1000)
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun saveChanges() {
        val username = usernameEditText.text.toString()
        val email = emailEditText.text.toString()
        val currentPassword = currentPasswordEditText.text.toString()
        val newPassword = newPasswordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        viewModel.updateProfile(
            newUsername = username,
            newEmail = email,
            currentPassword = currentPassword.ifBlank { null },
            newPassword = newPassword.ifBlank { null },
            confirmPassword = confirmPassword.ifBlank { null }
        )
    }

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar cuenta")
            .setMessage("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteAccount()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun navigateToAuth() {
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}