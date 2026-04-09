package com.parkway.demo.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.parkway.demo.R
import com.parkway.demo.utils.PreferencesManager

class DashboardActivity : AppCompatActivity() {

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var tvUserName: TextView
    private lateinit var btnLogout: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        preferencesManager = PreferencesManager(this)

        // Check if user is logged in
        if (!preferencesManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        initializeViews()
        setupUserInfo()
        setupClickListeners()
    }

    private fun initializeViews() {
        tvUserName = findViewById(R.id.tvUserName)
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun setupUserInfo() {
        val fullName = preferencesManager.getFullName()
        tvUserName.text = fullName
    }

    private fun setupClickListeners() {
        btnLogout.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogout() {
        preferencesManager.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
