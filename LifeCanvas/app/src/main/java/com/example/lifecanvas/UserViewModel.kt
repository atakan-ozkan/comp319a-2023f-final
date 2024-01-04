package com.example.lifecanvas

import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private var userModel: UserModel =UserModel("", "", "")
    fun initUser(firstName: String, lastName: String, password: String) {
        userModel = UserModel(firstName, lastName, hashPassword(password))
    }

    fun updateFullName(firstName: String, lastName: String) {
        userModel.firstName = firstName
        userModel.lastName = lastName
    }

    fun updatePassword(newPassword: String) {
        userModel.setPassword(hashPassword(newPassword))
    }

    fun verifyPassword(inputPassword: String): Boolean {
        return userModel.verifyPassword(inputPassword)
    }

    private fun hashPassword(password: String): String {
        return password.hashCode().toString()
    }

    fun getUserModel (): UserModel{
        return userModel
    }
}
