package com.example.emotify.Model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Repository class responsible for handling user-related data operations.
 * Manages storing and retrieving user data in Firebase Firestore.
 */

class UserRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Saves user data to Firestore under the "userData" collection.
     * If the user is authenticated, their data is stored using their UID as the document ID.
     * If successful, calls the onSuccess callback.
     * If an error occurs, calls the onFailure callback with the exception.
     *
     * @param user The user data to be stored.
     * @param onSuccess Callback function executed when data is saved successfully.
     * @param onFailure Callback function executed when an error occurs.
     */

    fun saveUserData(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("userData").document(userId)
                .set(user)
                .addOnSuccessListener {
                    Log.d("UserRepository", "User data saved successfully")
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    Log.e("UserRepository", "Error saving user data", exception)
                    onFailure(exception)
                }
        } else {
            onFailure(Exception("User not authenticated"))
        }
    }
}