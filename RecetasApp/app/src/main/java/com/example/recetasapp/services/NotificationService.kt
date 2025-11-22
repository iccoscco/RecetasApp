package com.example.recetasapp.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.recetasapp.MainActivity
import com.example.recetasapp.R

// Servicio de Android que crea y muestra notificaciones para la app
class NotificationService : Service() {
    
    companion object {
        private const val CHANNEL_ID = "recipe_reminders"
        private const val CHANNEL_NAME = "Recordatorios de Recetas"

        // Creación de notificaciones
        fun createNotification(context: Context, title: String, message: String) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notificaciones de recetas y recordatorios"
                }
                notificationManager.createNotificationChannel(channel)
            }
            
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
            
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }

    // Devuelve null porque este servicio no se comunica directamente con otras componentes.
    override fun onBind(intent: Intent?): IBinder? = null

    // Llama a createNotificacion para mostrar la notificación
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val title = intent?.getStringExtra("title") ?: "Cocina Total"
        val message = intent?.getStringExtra("message") ?: "Tienes una nueva notificación"
        
        createNotification(this, title, message)
        stopSelf()
        
        return START_NOT_STICKY
    }
}
