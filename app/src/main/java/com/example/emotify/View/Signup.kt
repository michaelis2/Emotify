package com.example.emotify.View
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.emotify.R
import com.example.emotify.ViewModel.SignupViewModel

/**
 * The `Signup` activity allows users to create a new account by providing an email,
 * password, and confirming the password. It uses `SignupViewModel` to handle authentication.
 */

class Signup : AppCompatActivity() {

    private val signupViewModel: SignupViewModel by viewModels() // ViewModel for handling signup logic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize UI components
        val emailField: EditText = findViewById(R.id.editTextTextEmailAddress2)
        val passwordField: EditText = findViewById(R.id.editTextTextPassword2)
        val confirmPasswordField: EditText = findViewById(R.id.editTextTextPassword3)
        val signupButton: Button = findViewById(R.id.button2)
        val loginButton: Button = findViewById(R.id.button3)

        signupButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            signupViewModel.signup(email, password, confirmPassword)
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        /**
         * Observes the signup result and displays a toast message.
         * If signup is successful, navigates to `getUserInfo` activity.
         */
        signupViewModel.signupResult.observe(this) { result ->
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            if (result == "Signup successful") {
                    val intent = Intent(this, getUserInfo::class.java)
                    startActivity(intent)
                    finish()

            }
        }
    }
}
