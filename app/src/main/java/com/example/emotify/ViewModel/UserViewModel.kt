package com.example.emotify.ViewModel
import androidx.lifecycle.ViewModel
import com.example.emotify.Model.User
import com.example.emotify.Model.UserRepository

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    fun saveUserData(username: String, age: Int, gender: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val user = User(username, age, gender)
        userRepository.saveUserData(user, onSuccess, onFailure)
    }
}