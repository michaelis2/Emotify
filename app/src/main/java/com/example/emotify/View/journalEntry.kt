package com.example.emotify.View

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.emotify.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class journalEntry : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var journalEntry: EditText
    private lateinit var saveButton: Button
    private lateinit var journalDate: TextView
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_entry)

        journalEntry = findViewById(R.id.journalEntry)
        saveButton = findViewById(R.id.saveButton)
        journalDate = findViewById(R.id.journalDate)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        selectedDate = intent.getStringExtra("selectedDate")

        if (selectedDate != null) {
            journalDate.text = selectedDate
            fetchJournalEntry(selectedDate!!)
        }

        saveButton.setOnClickListener {
            saveOrUpdateJournalEntry()
        }
    }

    private fun fetchJournalEntry(date: String) {
        val userId = auth.currentUser?.uid ?: return
        val docRef = db.collection("userEntry").document(userId).collection("entries").document(date)

        docRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                journalEntry.setText(document.getString("entry"))
            } else {
                journalEntry.setText("")  // No entry for this date
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load entry", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveOrUpdateJournalEntry() {
        val userId = auth.currentUser?.uid ?: return
        val entryText = journalEntry.text.toString()

        if (selectedDate != null && entryText.isNotBlank()) {
            val entryData = hashMapOf(
                "entry" to entryText,
                "date" to selectedDate
            )

            db.collection("userEntry").document(userId).collection("entries").document(selectedDate!!)
                .set(entryData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Entry saved", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save entry", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Entry is empty", Toast.LENGTH_SHORT).show()
        }
    }
}
