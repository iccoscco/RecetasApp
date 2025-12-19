package com.example.recetasapp.data.repository

import com.example.recetasapp.data.local.dao.UserDao
import com.example.recetasapp.data.local.entities.UserEntity
import com.example.recetasapp.data.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Lógica de autenticación
class AuthRepository(private val userDao: UserDao, private val supabaseClient: SupabaseClient) {

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
    suspend fun loginWithGoogle(idToken: String, nonce: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            // Llama a Supabase para iniciar sesión con el token
            supabaseClient.auth.signInWith(IDToken) {
                this.idToken = idToken
                this.provider = Google
                this.nonce = nonce
            }

            // Obtiene el usuario de Supabase después del login exitoso
            val supabaseUser = supabaseClient.auth.currentUserOrNull()
            if (supabaseUser != null) {
                // TODO: Buscar si el usuario ya existe en tu base de datos local por email.
                // Si no existe, créalo. Si existe, actualiza sus datos si es necesario.
                val user = User(
                    id = 0,
                    email = supabaseUser.email ?: "",
                    username = supabaseUser.userMetadata?.get("name")?.toString()?.replace("\"", "") ?: "Usuario de Google",
                    profileImageUrl = supabaseUser.userMetadata?.get("avatar_url")?.toString()?.replace("\"", ""),
                )
                // Por ahora, simplemente devolvemos el usuario mapeado
                Result.success(user)
            } else {
                Result.failure(Exception("No se pudo obtener el usuario de Supabase después del login."))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
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
