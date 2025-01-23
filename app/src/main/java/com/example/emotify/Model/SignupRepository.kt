package com.example.emotify.Model

import com.google.firebase.auth.FirebaseAuth

class SignupRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

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
