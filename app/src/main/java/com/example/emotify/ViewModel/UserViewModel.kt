package com.example.emotify.ViewModel
import androidx.lifecycle.ViewModel
import com.example.emotify.Model.User
import com.example.emotify.Model.UserRepository

/**
 * ViewModel responsible for managing user data.
 * It interacts with the UserRepository to save user details.
 */
class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    /**
     * Saves user data to the repository.
     *
     * @param username The user's name.
     * @param age The user's age.
     * @param gender The user's gender.
     * @param onSuccess A callback executed if data is saved successfully.
     * @param onFailure A callback executed if data saving fails, with an exception.
     */
    fun saveUserData(username: String, age: Int, gender: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val user = User(username, age, gender)
        userRepository.saveUserData(user, onSuccess, onFailure)
    }
}