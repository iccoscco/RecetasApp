package com.example.recetasapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Define la tabla users en Room.
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @ColumnInfo(name = "email")
    val email: String,
    
    @ColumnInfo(name = "password")
    val password: String,
    
    @ColumnInfo(name = "username")
    val username: String,
    
    @ColumnInfo(name = "profile_image")
    val profileImageUrl: String? = null,
    
    @ColumnInfo(name = "culinary_goal")
    val culinaryGoal: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
