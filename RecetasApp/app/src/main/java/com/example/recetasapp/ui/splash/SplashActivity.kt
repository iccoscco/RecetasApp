package com.example.recetasapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.recetasapp.MainActivity
import com.example.recetasapp.R
import com.example.recetasapp.ui.auth.AuthActivity
import com.example.recetasapp.utils.PreferencesHelper

/**
 * SplashActivity - Pantalla de bienvenida
 *
 * Esta es la actividad LAUNCHER de la aplicación.
 * Se muestra durante 2-3 segundos mientras verifica:
 * - Si el usuario está autenticado
 * - Carga inicial de recursos
 *
 * Redirige a:
 * - AuthActivity si NO está autenticado
 * - MainActivity si SÍ está autenticado
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var preferencesHelper: PreferencesHelper
    private val splashDuration = 2500L // 2.5 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar PreferencesHelper
        preferencesHelper = PreferencesHelper(this)

        // Aplicar modo oscuro guardado
        applyDarkModePreference()

        setContentView(R.layout.activity_splash)

        // Ocultar ActionBar
        supportActionBar?.hide()

        // Navegar después del delay
        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthenticationAndNavigate()
        }, splashDuration)
    }

    private fun applyDarkModePreference() {
        val isDarkMode = preferencesHelper.isDarkModeEnabled()
        if (isDarkMode) {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            )
        } else {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun checkAuthenticationAndNavigate() {
        val isLoggedIn = preferencesHelper.isLoggedIn()

        val intent = if (isLoggedIn) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, AuthActivity::class.java)
        }

        startActivity(intent)
        finish() // Cerrar Splash para que no se pueda volver con back
    }
}