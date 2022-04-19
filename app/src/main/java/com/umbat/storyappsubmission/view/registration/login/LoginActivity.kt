package com.umbat.storyappsubmission.view.registration.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.umbat.storyappsubmission.api.ActivityResponses
import com.umbat.storyappsubmission.api.ApiConfig
import com.umbat.storyappsubmission.databinding.ActivityLoginBinding
import com.umbat.storyappsubmission.model.TokenModel
import com.umbat.storyappsubmission.model.UserModel
import com.umbat.storyappsubmission.model.UserPreference
import com.umbat.storyappsubmission.view.ViewModelFactory
import com.umbat.storyappsubmission.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel
    private lateinit var token: TokenModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
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
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }

        loginViewModel.getToken().observe(this) { token ->
            this.token = token
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val token = binding.passwordEditText.text.toString()

            val service = ApiConfig().getApiService().loginAccount(email, password, token)

            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                email != user.email -> {
                    binding.emailEditTextLayout.error = "Email tidak sesuai"
                }
                password != user.password -> {
                    binding.passwordEditTextLayout.error = "Password tidak sesuai"
                }
                else -> {
                    loginViewModel.login()
                    loginViewModel.saveToken(TokenModel(name = String.toString(), userId = String.toString(), token = String.toString()))
                    service.enqueue(object : Callback<ActivityResponses.LoginUploadResponse> {
                        override fun onResponse(
                            call: Call<ActivityResponses.LoginUploadResponse>,
                            response: Response<ActivityResponses.LoginUploadResponse>
                        ) {
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle("Yeah!")
                                setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
                                setPositiveButton("Lanjut") { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null && !responseBody.error) {
                                    Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ActivityResponses.LoginUploadResponse>, t: Throwable) {
                            Toast.makeText(this@LoginActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }
}