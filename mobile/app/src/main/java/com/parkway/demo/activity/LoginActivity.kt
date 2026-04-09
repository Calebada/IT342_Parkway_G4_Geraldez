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
import com.parkway.demo.model.LoginRequest
import com.parkway.demo.utils.PreferencesManager
import com.parkway.demo.utils.ValidationUtils
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvError: TextView
    private lateinit var tvSuccess: TextView
    private lateinit var tvRegisterLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferencesManager = PreferencesManager(this)

        // Check if already logged in
        if (preferencesManager.isLoggedIn()) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvError = findViewById(R.id.tvError)
        tvSuccess = findViewById(R.id.tvSuccess)
        tvRegisterLink = findViewById(R.id.tvRegisterLink)
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            performLogin()
        }

        tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validate form
        val validation = ValidationUtils.validateLoginForm(email, password)
        if (!validation.isValid) {
            showError(validation.message)
            return
        }

        // Show loading
        btnLogin.isEnabled = false
        btnLogin.text = getString(R.string.loading)

        // Make API call
        lifecycleScope.launch {
            try {
                val loginRequest = LoginRequest(email, password)
                val response = RetrofitClient.apiService.loginUser(loginRequest)

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    preferencesManager.saveUser(loginResponse)
                    showSuccess("Login successful!")

                    // Navigate to dashboard after a short delay
                    etEmail.postDelayed({
                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                        finish()
                    }, 1000)
                } else {
                    val errorBody = response.errorBody()?.string()
                    showError(errorBody ?: "Login failed. Please try again.")
                    btnLogin.isEnabled = true
                    btnLogin.text = getString(R.string.login_button)
                }
            } catch (e: Exception) {
                showError("Network error: ${e.message}")
                btnLogin.isEnabled = true
                btnLogin.text = getString(R.string.login_button)
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
