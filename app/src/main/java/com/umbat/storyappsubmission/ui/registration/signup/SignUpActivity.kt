package com.umbat.storyappsubmission.ui.registration.signup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.umbat.storyappsubmission.R
import com.umbat.storyappsubmission.databinding.ActivitySignUpBinding
import com.umbat.storyappsubmission.ui.ViewModelFactory
import com.umbat.storyappsubmission.ui.registration.login.LoginActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private val signupViewModel: SignUpViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        showLoading()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        viewModelFactory = ViewModelFactory.getInstance(this)
    }

    private fun showLoading() {
        signupViewModel.showLoading.observe(this@SignUpActivity) {
            binding.progressBarSignUp.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun showToast() {
        signupViewModel.toastText.observe(this@SignUpActivity) { toastText ->
            Toast.makeText(
                this@SignUpActivity, toastText, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setId() {
        binding.apply {
            signupViewModel.registerAccount(
                nameEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = getString(R.string.input_name)
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.input_email)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.input_password)
                }

                else -> {
                    setId()
                    showToast()
                    intentActivity()
                }
            }
        }
    }

    private fun intentActivity() {
        signupViewModel.signUpResponse.observe(this@SignUpActivity) { response ->
            if (!response.error) {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}