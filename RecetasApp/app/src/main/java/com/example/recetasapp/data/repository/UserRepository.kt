package com.example.recetasapp.data.repository

import com.example.recetasapp.data.local.dao.UserDao
import com.example.recetasapp.data.local.entities.UserEntity
import com.example.recetasapp.data.model.User

// Capa intermedia entre ViewModel y la base de datos Room
// Convierte entidades de base de datos "UserEntity" en modelos de dominio "User"
class UserRepository(private val userDao: UserDao) {

    // Pide un UserEntity y lo convierte en User
    suspend fun getUserById(userId: Int): User? {
        val entity = userDao.getUserById(userId)
        return entity?.let {
            User(
                id = it.id,
                username = it.username,
                email = it.email,
                password = it.password
            )
        }
    }

    // Convierte un User en UserEntity y lo manda actualizar
    suspend fun updateUser(user: User): Boolean {
        return try {
            val entity = UserEntity(
                id = user.id,
                username = user.username,
                email = user.email,
                password = user.password
            )
            userDao.updateUser(entity)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Llama al DAO para eliminar un usuario
    suspend fun deleteUser(userId: Int): Boolean {
        return try {
            userDao.deleteUserById(userId)
            true
        } catch (e: Exception) {
            false
        }
    }
}