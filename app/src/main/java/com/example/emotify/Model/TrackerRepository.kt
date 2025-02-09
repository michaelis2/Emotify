package com.example.emotify.Model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrackerRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun saveOrUpdateEmotion(date: String, emotion: String) {
        val userId = auth.currentUser?.uid
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        if (userId == null) {
            Log.e("Tracker", "User not authenticated")
            return
        }

        val imagesCollection = db.collection("images")
            .document(userId)
            .collection("userImages")

        imagesCollection.get().addOnSuccessListener { documents ->
            var existingDocId: String? = null

            for (document in documents) {
                val timestamp = document.getLong("timestamp")
                if (timestamp != null) {
                    val docDate = dateFormat.format(Date(timestamp))
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
