package com.example.emotify.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emotify.Model.SignupRepository

class SignupViewModel : ViewModel() {

    private val repository = SignupRepository()

    private val _signupResult = MutableLiveData<String>()
    val signupResult: LiveData<String> get() = _signupResult

    fun signup(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _signupResult.value = "Fields cannot be empty"
            return
        }

        if (password != confirmPassword) {
            _signupResult.value = "Passwords do not match"
            return
        }

        repository.signup(email, password) { success, message ->
            if (success) {
                _signupResult.value = "Signup successful"
            } else {
                _signupResult.value = message
            }
        }
    }
}
