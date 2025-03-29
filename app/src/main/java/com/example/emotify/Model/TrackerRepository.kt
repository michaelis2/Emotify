package com.example.emotify.Model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repository class responsible for handling the user's emotion tracking data.
 */

class TrackerRepository {

    private val db = FirebaseFirestore.getInstance()  // Initialize Firestore database instance
    private val auth = FirebaseAuth.getInstance() // Initialize Firebase Authentication instance

    /**
     * Saves or updates the user's emotion entry for the given date.
     * If an image entry exists for the current date, it updates the emotion.
     * Otherwise, it creates a new entry with the emotion and timestamp.
     *
     * @param date The date for which the emotion is being logged.
     * @param emotion The detected emotion.
     */
    fun saveOrUpdateEmotion(date: String, emotion: String) {
        val userId = auth.currentUser?.uid
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        // If no user is logged in, log an error and exit the function
        if (userId == null) {
            Log.e("Tracker", "User not authenticated")
            return
        }

        // Reference to the user's "images/userImages" collection in Firestore
        val imagesCollection = db.collection("images")
            .document(userId)
            .collection("userImages")

        // Fetch all documents in the user's "userImages" collection
        imagesCollection.get().addOnSuccessListener { documents ->
            var existingDocId: String? = null

            // Iterate over each document to check if an entry exists for today
            for (document in documents) {
                val timestamp = document.getLong("timestamp")
                if (timestamp != null) {
                    val docDate = dateFormat.format(Date(timestamp))
                    // If the stored date matches today's date, save its document ID
                    if (docDate == currentDate) {
                        existingDocId = document.id // Store the matching document ID
                        break
                    }
                }
            }

            if (existingDocId != null) {
                // If an image exists, update only the emotion
                imagesCollection.document(existingDocId)
                    .update("emotion", emotion)
                    .addOnSuccessListener { Log.d("Tracker", "Emotion updated: $emotion") }
                    .addOnFailureListener { e -> Log.e("Tracker", "Error updating emotion", e) }
            } else {
                // If no image exists, log a new entry with just emotion and timestamp
                val newEntry = mapOf(
                    "userId" to userId,
                    "date" to date,
                    "emotion" to emotion,
                    "timestamp" to System.currentTimeMillis()
                )
                imagesCollection.document(date)
                    .set(newEntry)
                    .addOnSuccessListener { Log.d("Tracker", "Emotion logged: $emotion") }
                    .addOnFailureListener { e -> Log.e("Tracker", "Error logging emotion", e) }
            }
        }.addOnFailureListener { e ->
            Log.e("Tracker", "Error fetching documents: ${e.message}")
        }
    }
}
