package com.example.emotify.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.emotify.R
import com.example.emotify.ViewModel.CameraViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class cameraResult : AppCompatActivity() {
    private val cameraViewModel: CameraViewModel by viewModels()
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_result)

        val imageView = findViewById<ImageView>(R.id.imageView3)
        val textView = findViewById<TextView>(R.id.emotion_result)
        val uploadButton = findViewById<TextView>(R.id.saveImage)
        // Get the passed data
        val imagePath = intent.getStringExtra("imagePath")
        val emotion = intent.getStringExtra("emotion")

        Log.d("CameraResult", "Image path: $imagePath")
        Log.d("CameraResult", "Emotion: $emotion")

        Glide.with(this).clear(imageView)
        // Load image into ImageView using Glide
        if (imagePath != null) {
            imageFile = File(imagePath)
            Glide.with(this)
                .load(imageFile)
                .into(imageView)

        }

        // Set detected emotion
        textView.text = emotion ?: "Emotion not detected"
        uploadButton.setOnClickListener {
            imageFile?.let { file ->
                uploadImage(file, emotion)
                // After uploading, navigate to the TrackerCalendar activity
                navigateToTrackerCalendar()
            } ?: Toast.makeText(this, "No image to upload!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun uploadImage(imageFile: File, emotion: String?) {
        val fileUri = Uri.fromFile(imageFile)
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return // Get current user ID
        val firestore = FirebaseFirestore.getInstance()

        // Reference the user's images collection
        val userImagesRef = firestore.collection("images").document(userId).collection("userImages")

        // Check if an image already exists for this date
        userImagesRef.get()
            .addOnSuccessListener { documents ->
                val existingImage = documents.documents.find {
                    val timestamp = it.getLong("timestamp") ?: 0L
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
                    date == currentDate
                }

                if (existingImage != null) {
                    Toast.makeText(this, "You already saved an image for today!", Toast.LENGTH_SHORT).show()
                } else {
                    // Save the image and emotion to Firestore
                    cameraViewModel.saveImageDataToFirestore(fileUri, imageFile.name, emotion ?: "Unknown")
                    Toast.makeText(this, "Uploading image...", Toast.LENGTH_SHORT).show()
                    navigateToTrackerCalendar()
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error checking existing images", e)
                Toast.makeText(this, "Error checking existing images", Toast.LENGTH_SHORT).show()
            }
    }


    private fun navigateToTrackerCalendar() {
        // Create an Intent to navigate to TrackerCalendar activity
        val intent = Intent(this, trackerCalendar::class.java)

        // Start the TrackerCalendar activity
        startActivity(intent)
        finish()  // Close this activity if needed
    }
}