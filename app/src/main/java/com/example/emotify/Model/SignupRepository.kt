package com.example.emotify.Model

import com.google.firebase.auth.FirebaseAuth

/**
 * Repository class responsible for handling user sign-up using Firebase Authentication.
 */

class SignupRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Signs up a new user using Firebase Authentication.
     *
     * @param email The user's email address.
     * @param password The user's chosen password.
     * @param callback A lambda function that returns a Boolean (success/failure) and a message.
     */
    fun signup(email: String, password: String, callback: (Boolean, String) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "")
                } else {
                    callback(false, task.exception?.message ?: "Signup failed")
                }
            }
    }
}
