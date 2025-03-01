package com.example.emotify.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.emotify.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Settings : Fragment() {

    private lateinit var usernameTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var trackerCalButton: Button
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize TextViews
        usernameTextView = view.findViewById(R.id.usernameTextview)
        ageTextView = view.findViewById(R.id.ageTextview)
        genderTextView = view.findViewById(R.id.genderTextview)
        trackerCalButton= view.findViewById(R.id.button4)

        trackerCalButton.setOnClickListener {
            val intent = Intent(activity, trackerHistory::class.java)
            startActivity(intent)
        }
        // Fetch user data from Firebase
        loadUserData()

        return view
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            db.collection("userData").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val username = document.getString("username") ?: "N/A"
                        val age = document.getLong("age")?.toString() ?: "N/A"
                        val gender = document.getString("gender") ?: "N/A"

                        // Set data to TextViews
                        usernameTextView.text = username
                        ageTextView.text = "Age: $age"
                        genderTextView.text = "Gender: $gender"
                    } else {
                        usernameTextView.text = "User not found"
                    }
                }
                .addOnFailureListener {
                    usernameTextView.text = "Failed to load data"
                }
        } else {
            usernameTextView.text = "Not logged in"
        }
    }
}
