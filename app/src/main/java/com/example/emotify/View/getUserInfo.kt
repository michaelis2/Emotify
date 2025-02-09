package com.example.emotify.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import com.example.emotify.R
import com.example.emotify.ViewModel.UserViewModel

class getUserInfo : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var womanRadioButton: RadioButton
    private lateinit var manRadioButton: RadioButton
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_user_info)

        usernameEditText = findViewById(R.id.enterUsername)
        ageEditText = findViewById(R.id.enterAge)
        genderRadioGroup = findViewById(R.id.genderRadioGroup)
        womanRadioButton = findViewById(R.id.womanRadiobutton)
        manRadioButton = findViewById(R.id.manRadiobutton)

        val proceedButton: Button = findViewById(R.id.proceedButton)
        proceedButton.setOnClickListener {
            saveUserData()
        }
    }

    private fun saveUserData() {
        val username = usernameEditText.text.toString().trim()
        val age = ageEditText.text.toString().toIntOrNull()

        // Ensure a gender is selected
        val gender = when (genderRadioGroup.checkedRadioButtonId) {
            R.id.womanRadiobutton -> "Woman"
            R.id.manRadiobutton -> "Man"
            else -> ""
        }

        // Check if any field is missing
        if (username.isEmpty() || age == null || gender.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        userViewModel.saveUserData(username, age, gender, {
            Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show()

            // Navigate to MainActivity
            val intent = Intent(this@getUserInfo, MainActivity::class.java)
            startActivity(intent)
            finish()  // Finish this activity so it’s removed from the back stack

        }, { exception ->
            Toast.makeText(this, "Error saving user data: ${exception.message}", Toast.LENGTH_SHORT).show()
        })
    }
}