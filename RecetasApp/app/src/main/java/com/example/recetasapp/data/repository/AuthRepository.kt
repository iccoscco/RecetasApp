package com.example.recetasapp.data.repository

import com.example.recetasapp.data.local.dao.UserDao
import com.example.recetasapp.data.local.entities.UserEntity
import com.example.recetasapp.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Lógica de autenticación
class AuthRepository(private val userDao: UserDao) {

    // Logearse
    suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val userEntity = userDao.login(email, password)
            if (userEntity != null) {
                Result.success(userEntity.toUser()) // Si existe
            } else {
                Result.failure(Exception("Credenciales incorrectas")) // Si no existe
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Register
    suspend fun register(
        email: String,
        password: String,
        username: String
    ): Result<User> = withContext(Dispatchers.IO) {
        try {
            // Verificar si el email ya existe
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                return@withContext Result.failure(Exception("El email ya está registrado"))
            }
            // Crear nuevo usuario
            val userEntity = UserEntity(
                email = email,
                password = password, // Se debería hashear la contraseña en producción, pero podemos ignorarlo 7u7
                username = username
            )
            // Guardar en la base de datos
            val userId = userDao.insertUser(userEntity)
            val newUser = userDao.getUserById(userId.toInt())

            if (newUser != null) {
                Result.success(newUser.toUser())
            } else {
                Result.failure(Exception("Error al crear usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Implementar (Login Google)
    suspend fun loginWithGoogle(): Result<User> {
        // Amor de lejos amor de pend.... lalalal
        return Result.failure(Exception("Login con Google aún no implementado"))
    }

    // Implementar (Login Facebook)
    suspend fun loginWithFacebook(): Result<User> {
        // Me gusta la carne la leche y el pan
        return Result.failure(Exception("Login con Facebook aún no implementado"))
    }

    // Mapper: Conversión de un UserEntity(db) a al modelo de la App(User)
    private fun UserEntity.toUser(): User {
        return User(
            id = id,
            email = email,
            username = username,
            profileImageUrl = profileImageUrl,
            culinaryGoal = culinaryGoal,
            createdAt = createdAt
        )
    }
}
