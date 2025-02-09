package com.example.emotify.ViewModel

import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class CameraViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val client = OkHttpClient()

   /* fun sendImageToFlaskAPI(
        imageData: File,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            // Create RequestBody for the image
            val requestBody = imageData.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.jpg", requestBody)
                .build()

            // Build the POST request
            val request = Request.Builder()
                .url("http://10.0.2.2:5000/predict")
                // Replace with your Flask server URL
                .post(multipartBody)
                .build()

            // Execute the request asynchronously
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    response.body?.let { responseBody ->
                        val jsonResponse = JSONObject(responseBody.string())
                        val emotion = jsonResponse.getString("emotion")
                        onSuccess(emotion)
                    } ?: onFailure(Exception("Empty response body"))
                }

                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    onFailure(e)
                }
            })
        } catch (e: Exception) {
            onFailure(e)
        }
    }
*/
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
    private fun getImageFileName(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "IMG_$timestamp.jpg"
    }
}
