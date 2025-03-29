package com.example.emotify.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.emotify.R
import com.example.emotify.ViewModel.LoginViewModel

/**
 * Login Activity that allows users to enter their email and password
 * to authenticate with the application.
 */

class Login : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailField: EditText = findViewById(R.id.editTextTextEmailAddress)
        val passwordField: EditText = findViewById(R.id.editTextTextPassword)
        val loginButton: Button = findViewById(R.id.button)

        // Set a click listener for the login button
        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.login(email, password)
            }
        }

        // Observe login result from ViewModel
        loginViewModel.loginResult.observe(this) { result ->
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            if (result == "Login successful") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent) // Navigate to MainActivity on successful login
                finish() // Close the login activity
            }
        }
    }
}
