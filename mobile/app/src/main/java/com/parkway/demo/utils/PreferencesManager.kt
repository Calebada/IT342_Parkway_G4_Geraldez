package com.parkway.demo.utils

import android.content.Context
import android.content.SharedPreferences
import com.parkway.demo.model.LoginResponse

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("parkway_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_EMAIL = "email"
        private const val KEY_FIRSTNAME = "firstname"
        private const val KEY_LASTNAME = "lastname"
        private const val KEY_ROLE = "role"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // Save user data
    fun saveUser(loginResponse: LoginResponse) {
        with(sharedPreferences.edit()) {
            putLong(KEY_USER_ID, loginResponse.getActualId() ?: 0L)
            putString(KEY_EMAIL, loginResponse.email)
            putString(KEY_FIRSTNAME, loginResponse.firstname)
            putString(KEY_LASTNAME, loginResponse.lastname)
            putString(KEY_ROLE, loginResponse.role)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    // Get user email
    fun getUserEmail(): String? = sharedPreferences.getString(KEY_EMAIL, null)

    // Get user ID
    fun getUserId(): Long = sharedPreferences.getLong(KEY_USER_ID, 0L)

    // Get full name
    fun getFullName(): String {
        val firstname = sharedPreferences.getString(KEY_FIRSTNAME, "")
        val lastname = sharedPreferences.getString(KEY_LASTNAME, "")
        return "$firstname $lastname"
    }

    // Get role
    fun getUserRole(): String? = sharedPreferences.getString(KEY_ROLE, null)

    // Check if user is logged in
    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)

    // Logout user (clear all data)
    fun logout() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    // Clear all preferences
    fun clearAll() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}
