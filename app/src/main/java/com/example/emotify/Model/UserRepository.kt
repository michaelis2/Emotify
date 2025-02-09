package com.example.emotify.Model
import android.util.Log
import com.example.emotify.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

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