package com.example.lifecanvas.model

data class UserModel(
    var firstName: String,
    var lastName: String,
    var passwordHash: String
) {
    fun setPassword(password: String) {
        this.passwordHash = hashPassword(password)
    }
    fun verifyPassword(inputPassword: String): Boolean {
        return hashPassword(inputPassword) == passwordHash
    }
    private fun hashPassword(password: String): String {
        return password.hashCode().toString()
    }
}
