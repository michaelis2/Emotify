package com.example.emotify.Model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * Repository class responsible for handling user authentication via Firebase.
 * This class interacts with Firebase Authentication to log in users.
 */

class LoginRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Logs in a user using Firebase Authentication.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [FirebaseUser] object if login is successful, or null if login fails.
     */
    suspend fun loginUser(email: String, password: String): FirebaseUser? {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            authResult.user
        } catch (e: Exception) {
            null
        }
    }
}
