package com.example.emotify.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emotify.Model.LoginRepository
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling user login functionality.
 */
class LoginViewModel : ViewModel() {

    private val repository = LoginRepository()

    private val _loginResult = MutableLiveData<String>()  // MutableLiveData to store login result messages
    val loginResult: LiveData<String> get() = _loginResult  // Public LiveData to observe login result updates from the UI

    /**
     * Attempts to log in the user with the provided email and password.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {  // Launching a coroutine within the ViewModelScope to handle the login process asynchronously
            val user = repository.loginUser(email, password) // Update the LiveData based on the login success or failure
            if (user != null) {
                _loginResult.value = "Login successful"
            } else {
                _loginResult.value = "Login failed"
            }
        }
    }
}
