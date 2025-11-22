package com.example.recetasapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*

// Servicio de Android en segundo plano que se encarga de sincronizar datos de la app con un servidor, como:
class SyncService : Service() {

    // Corutina para correr hilos y manejar errores sin que afecte al sistema
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // Creación del servicio
    override fun onCreate() {
        super.onCreate()
        startSync() // Inicio de sincronización
    }

    // Devuelve "START_STICKY": el servicio se reinicia si Android lo mata por recursos.
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    // Devuelve null porque este servicio no se comunica con la UI.
    override fun onBind(intent: Intent?): IBinder? = null

    // Llama una corutina
    private fun startSync() {
        serviceScope.launch {
            try {
                // Implementar sincronización de datos
                syncFavorites()
                syncWeeklyPlans()
                syncShoppingLists()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                stopSelf()
            }
        }
    }
    
    private suspend fun syncFavorites() {
        // Sincronizar favoritos con servidor
        delay(1000)
    }
    
    private suspend fun syncWeeklyPlans() {
        // Sincronizar planes semanales
        delay(1000)
    }
    
    private suspend fun syncShoppingLists() {
        // Sincronizar listas de compras
        delay(1000)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
