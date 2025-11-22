package com.example.recetasapp.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recetasapp.R

// Maneja la autenticación del login y register
class AuthActivity : AppCompatActivity() {

    // Crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        
        if (savedInstanceState == null) {
            showLoginFragment()
        }
    }

    // Reemplaza el contenedor R.id.auth_container con el fragmento LoginFragment
    fun showLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_container, LoginFragment())
            .commit()
    }

    // Reemplaza el contenedor R.id.auth_container con el fragmento RegisterFragment
    fun showRegisterFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_container, RegisterFragment())
            .addToBackStack(null)
            .commit()
    }
}
