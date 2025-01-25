package com.example.emotify.ViewModel

import android.util.Base64
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CameraViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    fun uploadImageToDatabase(
        imageData: ByteArray,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("User not authenticated"))
            return
        }

        val fileName = getImageFileName()
        val base64Image = Base64.encodeToString(imageData, Base64.DEFAULT)

        // Create metadata to store in Firestore
        val metadata = mapOf(
            "userId" to userId,
            "fileName" to fileName,
            "imageData" to base64Image, // Storing image as Base64
            "timestamp" to System.currentTimeMillis()
        )

        // Save metadata (including image data) in Firestore
        firebaseFirestore.collection("images")
            .document(userId) // Ensures the 'userId' document exists
            .collection("userImages")
            .document(fileName) // Use fileName as the document ID for clarity
            .set(metadata) // Use 'set()' instead of 'add()'
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    private fun getImageFileName(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "IMG_$timestamp.jpg"
    }
}
