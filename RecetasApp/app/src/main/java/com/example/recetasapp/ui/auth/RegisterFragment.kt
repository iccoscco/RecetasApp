package com.example.recetasapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recetasapp.MainActivity
import com.example.recetasapp.R
import com.example.recetasapp.data.local.database.DatabaseBuilder
import com.example.recetasapp.data.repository.AuthRepository
import com.example.recetasapp.utils.PreferencesHelper
import com.example.recetasapp.utils.isValidEmail
import com.example.recetasapp.utils.isValidPassword
import com.example.recetasapp.utils.showToast

// Fragment que maneja la pantalla de registro de usuarios
class RegisterFragment : Fragment() {
    
    private lateinit var viewModel: AuthViewModel
    private lateinit var preferencesHelper: PreferencesHelper
    
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView

    // Infla el layout fragment_register y devuelve la vista
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    // Inicializa el ViewModel (initViewModel) y configura observadores (setupObservers) y listeners de botones (setupListeners).
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initViewModel()
        initViews(view)
        setupObservers()
        setupListeners()
    }

    //
    private fun initViewModel() {
        val database = DatabaseBuilder.getInstance(requireContext())
        val repository = AuthRepository(database.userDao()) // Crea el AuthRepository
        val factory = AuthViewModelFactory(repository) //Crea el AuthViewModel
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        
        preferencesHelper = PreferencesHelper(requireContext())
    }

    // Asocia las variables de la clase con los elementos de la UI del layout.
    private fun initViews(view: View) {
        usernameEditText = view.findViewById(R.id.username_edit_text)
        emailEditText = view.findViewById(R.id.email_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)
        confirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text)
        registerButton = view.findViewById(R.id.register_button)
        loginLink = view.findViewById(R.id.login_link)
    }
    
    private fun setupObservers() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { user ->
                preferencesHelper.saveUser(user)
                requireContext().showToast("¡Registro exitoso!")
                navigateToMain()
            }.onFailure { error ->
                requireContext().showToast("Error: ${error.message}")
            }
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            registerButton.isEnabled = !isLoading
        }
    }
    
    private fun setupListeners() {
        registerButton.setOnClickListener {
            attemptRegister()
        }
        
        loginLink.setOnClickListener {
            (activity as? AuthActivity)?.showLoginFragment()
        }
    }
    
    private fun attemptRegister() {
        val username = usernameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()
        
        // Validaciones
        if (username.isEmpty()) {
            usernameEditText.error = "Ingresa tu nombre de usuario"
            return
        }
        
        if (email.isEmpty()) {
            emailEditText.error = "Ingresa tu email"
            return
        }
        
        if (!email.isValidEmail()) {
            emailEditText.error = "Email inválido"
            return
        }
        
        if (password.isEmpty()) {
            passwordEditText.error = "Ingresa tu contraseña"
            return
        }
        
        if (!password.isValidPassword()) {
            passwordEditText.error = "La contraseña debe tener al menos 6 caracteres"
            return
        }
        
        if (password != confirmPassword) {
            confirmPasswordEditText.error = "Las contraseñas no coinciden"
            return
        }
        
        // Ejecutar registro
        viewModel.register(email, password, username)
    }
    
    private fun navigateToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
