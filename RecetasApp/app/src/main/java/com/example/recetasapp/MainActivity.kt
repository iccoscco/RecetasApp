package com.example.recetasapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.NavigationUI
import androidx.activity.addCallback

/**
 * MainActivity - Actividad principal de la aplicación
 *
 * IMPORTANTE: Esta clase permanece en la raíz del paquete (com.example.recetasapp)
 * NO debe moverse a ninguna subcarpeta.
 *
 * Responsabilidades:
 * - Host del NavHostFragment
 * - Configuración de Bottom Navigation (4 ítems principales)
 * - Configuración de Navigation Drawer (opciones secundarias)
 * - Manejo de navegación global
 */
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController // controla la navegación entre fragments.
    private lateinit var appBarConfiguration: AppBarConfiguration // define los destinos principales(home, search, etc...)
    private var drawerLayout: DrawerLayout? = null // el contenedor del menú latera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ocultar el ActionBar
        supportActionBar?.hide()

        setupNavigation()
        setupBottomNavigation()
        setupNavigationDrawer()
    }

    // Configura el NavController
    private fun setupNavigation() {
        // Obtener NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Configurar ActionBar con Navigation
        // Los top-level destinations no mostrarán botón back
        drawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.searchFragment,
                R.id.communityFragment,
                R.id.profileFragment
            ),
            drawerLayout
        )
        // Se traba esta cosa xd
        //setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.setupWithNavController(navController)

        // Ocultar bottom navigation en ciertas pantallas
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.recipeDetailFragment,
                R.id.settingsFragment,
                R.id.uploadRecipeFragment -> {
                    bottomNav?.visibility = android.view.View.GONE
                }
                else -> {
                    bottomNav?.visibility = android.view.View.VISIBLE
                }
            }
        }
    }

    private fun setupNavigationDrawer() {
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView?.let {
            NavigationUI.setupWithNavController(it, navController)
        }

        // Manejar el botón back con el nuevo sistema
        onBackPressedDispatcher.addCallback(this) {
            if (drawerLayout?.isDrawerOpen(androidx.core.view.GravityCompat.START) == true) {
                drawerLayout?.closeDrawer(androidx.core.view.GravityCompat.START)
            } else {
                if (!navController.navigateUp()) {
                    finish()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}