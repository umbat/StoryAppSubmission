package com.umbat.storyappsubmission.ui.registration.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umbat.storyappsubmission.databinding.ActivityWelcomeBinding
import com.umbat.storyappsubmission.ui.registration.login.LoginActivity
import com.umbat.storyappsubmission.ui.registration.signup.SignUpActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}