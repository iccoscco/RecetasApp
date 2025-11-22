package com.example.recetasapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recetasapp.data.local.dao.UserDao
import com.example.recetasapp.data.local.entities.UserEntity

// Crea la base de datos
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
