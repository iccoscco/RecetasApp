package com.example.recetasapp.ui.cookingmode

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.recetasapp.R
import com.example.recetasapp.utils.Constants

// Foreground Service que mantiene activo el modo cocina
class CookingModeService : Service() {

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "cooking_mode_channel"
        private const val CHANNEL_NAME = "Modo Cocina"
    }

    // Crea el canal de notificación para Android
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    // Crea la notificación y inicia el servicio
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // Compatibilidad: Obligatorio en Android O+
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Modo Cocina activo"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Modo Cocina Activo")
            .setContentText("Temporizador en progreso")
            .setSmallIcon(R.drawable.ic_cooking)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}