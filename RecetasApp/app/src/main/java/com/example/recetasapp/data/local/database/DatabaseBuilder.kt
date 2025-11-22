package com.example.recetasapp.data.local.database

import android.content.Context
import androidx.room.Room

// Crea y devuelve una instancia de AppDatabase y administra el ciclo de vida de la db
object DatabaseBuilder {
    
    private var INSTANCE: AppDatabase? = null
    private const val DATABASE_NAME = "cocina_total_db"

    // Devuelve la única instancia de la base de datos. (Singleton)
    fun getInstance(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = buildDatabase(context)
            INSTANCE = instance
            instance
        }
    }

    // Crea la base de datos Room con el nombre "cocina_total_db" y permite borrar y recrear la db
    private fun buildDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    // Elimina la instancia guardada para crearse una nueva
    fun destroyInstance() {
        INSTANCE = null
    }
}
