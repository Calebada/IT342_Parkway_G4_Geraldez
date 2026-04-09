package com.parkway.demo.utils

import android.util.Patterns

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidName(name: String): Boolean {
        return name.isNotEmpty() && name.length >= 2
    }

    fun validateLoginForm(email: String, password: String): ValidationResult {
        return when {
            email.isEmpty() -> ValidationResult(false, "Email is required")
            !isValidEmail(email) -> ValidationResult(false, "Invalid email format")
            password.isEmpty() -> ValidationResult(false, "Password is required")
            else -> ValidationResult(true, "")
        }
    }

    fun validateRegistrationForm(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        confirmPassword: String
    ): ValidationResult {
        return when {
            firstname.isEmpty() -> ValidationResult(false, "First name is required")
            !isValidName(firstname) -> ValidationResult(false, "First name must be at least 2 characters")
            lastname.isEmpty() -> ValidationResult(false, "Last name is required")
            !isValidName(lastname) -> ValidationResult(false, "Last name must be at least 2 characters")
            email.isEmpty() -> ValidationResult(false, "Email is required")
            !isValidEmail(email) -> ValidationResult(false, "Invalid email format")
            password.isEmpty() -> ValidationResult(false, "Password is required")
            !isValidPassword(password) -> ValidationResult(false, "Password must be at least 6 characters")
            password != confirmPassword -> ValidationResult(false, "Passwords do not match")
            else -> ValidationResult(true, "")
        }
    }
}

data class ValidationResult(val isValid: Boolean, val message: String)
