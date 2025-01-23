package com.example.emotify.Model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class LoginRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Function to log in the user
    suspend fun loginUser(email: String, password: String): FirebaseUser? {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            authResult.user
        } catch (e: Exception) {
            null
        }
    }
}
