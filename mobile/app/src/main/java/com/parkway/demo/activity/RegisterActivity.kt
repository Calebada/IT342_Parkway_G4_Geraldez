package com.parkway.demo.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.parkway.demo.R
import com.parkway.demo.api.RetrofitClient
import com.parkway.demo.model.User
import com.parkway.demo.utils.ValidationUtils
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvError: TextView
    private lateinit var tvSuccess: TextView
    private lateinit var tvLoginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        etUsername = findViewById(R.id.etUsername)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvError = findViewById(R.id.tvError)
        tvSuccess = findViewById(R.id.tvSuccess)
        tvLoginLink = findViewById(R.id.tvLoginLink)
    }

    private fun setupClickListeners() {
        btnRegister.setOnClickListener {
            performRegistration()
        }

        tvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun performRegistration() {
        val username = etUsername.text.toString().trim()
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        // Validate form
        val validation = ValidationUtils.validateRegistrationForm(
            firstName, lastName, email, password, confirmPassword
        )
        if (!validation.isValid) {
            showError(validation.message)
            return
        }

        // Show loading
        btnRegister.isEnabled = false
        btnRegister.text = getString(R.string.loading)

        // Make API call
        lifecycleScope.launch {
            try {
                val user = User(
                    firstname = firstName,
                    lastname = lastName,
                    email = email,
                    password = password
                )

                val response = RetrofitClient.apiService.registerUser(user)

                if (response.isSuccessful) {
                    showSuccess("Registration successful! Redirecting to login...")

                    // Navigate to login after a short delay
                    etEmail.postDelayed({
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }, 2000)
                } else {
                    val errorBody = response.errorBody()?.string()
                    showError(errorBody ?: "Registration failed. Please try again.")
                    btnRegister.isEnabled = true
                    btnRegister.text = getString(R.string.register_button)
                }
            } catch (e: Exception) {
                showError("Network error: ${e.message}")
                btnRegister.isEnabled = true
                btnRegister.text = getString(R.string.register_button)
            }
        }
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
        tvSuccess.visibility = View.GONE
    }

    private fun showSuccess(message: String) {
        tvSuccess.text = message
        tvSuccess.visibility = View.VISIBLE
        tvError.visibility = View.GONE
    }

    private fun clearMessages() {
        tvError.visibility = View.GONE
        tvSuccess.visibility = View.GONE
    }
}
