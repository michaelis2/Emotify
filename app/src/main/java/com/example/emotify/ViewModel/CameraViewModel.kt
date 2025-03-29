package com.example.emotify.ViewModel

import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * ViewModel responsible for handling image processing and storing image data in Firestore.
 */

class CameraViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Saves image data along with the detected emotion to Firestore.
     *
     * @param imageUri The URI of the captured image.
     * @param fileName The name of the image file.
     * @param emotion The detected emotion from the image.
     */
    fun saveImageDataToFirestore(imageUri: Uri, fileName: String, emotion: String) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            return
        }

        try {
            // Convert image to Base64
            val imageFile = File(imageUri.path ?: "")
            val imageData = encodeImageToBase64(imageFile)

            // Create metadata to store in Firestore
            val metadata = mapOf(
                "userId" to userId,
                "fileName" to fileName,
                "imageData" to imageData, // Storing image as Base64
                "emotion" to emotion, // Store detected emotion
                "timestamp" to System.currentTimeMillis()
            )

            // Save metadata in Firestore
            firebaseFirestore.collection("images")
                .document(userId)
                .collection("userImages")
                .document(fileName)
                .set(metadata)
                .addOnSuccessListener {
                    println("Image and emotion successfully saved")
                }
                .addOnFailureListener { e ->
                    println("Error saving image: ${e.message}")
                }
        } catch (e: Exception) {
            println("Failed to encode image: ${e.message}")
        }
    }

    /**
     * Converts an image file to a Base64-encoded string.
     *
     * @param file The image file to encode.
     * @return Base64 string representation of the image, or null if encoding fails.
     */
    private fun encodeImageToBase64(file: File): String? {
        return try {
            val inputStream = FileInputStream(file)
            val bytes = inputStream.readBytes()
            inputStream.close()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: IOException) {
            println("Error reading file: ${e.message}")
            null
        }
    }
}
