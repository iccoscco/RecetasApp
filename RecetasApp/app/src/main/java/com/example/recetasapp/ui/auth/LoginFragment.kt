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

// Fragmen que representa la pantalla de inicio de sesión de la app.
class LoginFragment : Fragment() {
    
    private lateinit var viewModel: AuthViewModel
    private lateinit var preferencesHelper: PreferencesHelper
    
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView
    private lateinit var facebookButton: Button
    private lateinit var googleButton: Button
    private lateinit var guestButton: Button

    // Infla el layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    // Inicializa el ViewModel (initViewModel) y configura observadores (setupObservers) y listeners de botones (setupListeners).
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initViewModel()
        initViews(view)
        setupObservers()
        setupListeners()
    }
    private fun initViewModel() {
        val database = DatabaseBuilder.getInstance(requireContext())
        val repository = AuthRepository(database.userDao()) // Crea el AuthRepository
        val factory = AuthViewModelFactory(repository) //Crea el AuthViewModel
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        
        preferencesHelper = PreferencesHelper(requireContext()) // Preferences helper para guardar el usuario
    }

    // Asocia las variables de la clase con los elementos de la UI del layout
    private fun initViews(view: View) {
        emailEditText = view.findViewById(R.id.email_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)
        loginButton = view.findViewById(R.id.login_button)
        registerLink = view.findViewById(R.id.register_link)
        facebookButton = view.findViewById(R.id.facebook_button)
        googleButton = view.findViewById(R.id.google_button)
        guestButton = view.findViewById(R.id.guest_button)
    }

    // Observa si se realizo el login
    private fun setupObservers() {
        // Observa si se hizo un login exitoso para guardar los datos y manda al MainActivity
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { user ->
                preferencesHelper.saveUser(user)
                requireContext().showToast("¡Bienvenido ${user.username}!")
                navigateToMain()
            }.onFailure { error ->
                requireContext().showToast("Error: ${error.message}")
            }
        }

        // Desactiva el boton login mientras se realiza la operación
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loginButton.isEnabled = !isLoading
        }
    }

    // Configuración de los botones
    private fun setupListeners() {
        loginButton.setOnClickListener {
            attemptLogin()
        }
        
        registerLink.setOnClickListener {
            (activity as? AuthActivity)?.showRegisterFragment()
        }
        
        facebookButton.setOnClickListener {
            requireContext().showToast("Facebook login próximamente")
        }
        
        googleButton.setOnClickListener {
            requireContext().showToast("Google login próximamente")
        }
        
        guestButton.setOnClickListener {
            navigateToMain()
        }
    }

    // Valida y limpia los datos
    private fun attemptLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString()
        
        if (email.isEmpty()) {
            emailEditText.error = "Ingresa tu email"
            return
        }
        
        if (!email.isValidEmail()) { // De Utils
            emailEditText.error = "Email inválido"
            return
        }
        
        if (password.isEmpty()) {
            passwordEditText.error = "Ingresa tu contraseña"
            return
        }
        
        if (!password.isValidPassword()) { // De Utils
            passwordEditText.error = "La contraseña debe tener al menos 6 caracteres"
            return
        }
        
        // Ejecutar login
        viewModel.login(email, password)
    }
    
    private fun navigateToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
