package com.example.emotify.Model

/**
 * Data class representing a user profile.
 *
 * @property username The name of the user.
 * @property age The age of the user.
 * @property gender The gender of the user.
 */

data class User(
    val username: String = "",
    val age: Int = 0,
    val gender: String = ""
)
