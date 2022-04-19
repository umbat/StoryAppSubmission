package com.umbat.storyappsubmission.view.registration.signup

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.umbat.storyappsubmission.api.ActivityResponses
import com.umbat.storyappsubmission.api.ApiConfig
import com.umbat.storyappsubmission.databinding.ActivitySignUpBinding
import com.umbat.storyappsubmission.model.UserModel
import com.umbat.storyappsubmission.model.UserPreference
import com.umbat.storyappsubmission.view.ViewModelFactory
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signupViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
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
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignUpViewModel::class.java]
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            val service = ApiConfig().getApiService().registerAccount(name, email, password)

            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }

                else -> {
                    signupViewModel.saveUser(UserModel(name, email, password, false))
                    service.enqueue(object : Callback<ActivityResponses.SignUpUploadResponse> {
                        override fun onResponse(
                            call: Call<ActivityResponses.SignUpUploadResponse>,
                            response: Response<ActivityResponses.SignUpUploadResponse>
                        ) {
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null && !responseBody.error) {
                                    Toast.makeText(this@SignUpActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@SignUpActivity, response.message(), Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ActivityResponses.SignUpUploadResponse>, t: Throwable) {
                            Toast.makeText(this@SignUpActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

//    private fun makeAccount() {
//        val name = String.toString()
//        val email = String.toString()
//        val password = String.toString()
//
//        val service = ApiConfig().getApiService().registerAccount(name, email, password)
//        service.enqueue(object : Callback<ActivityResponses.SignUpUploadResponse> {
//            override fun onResponse(
//                call: Call<ActivityResponses.SignUpUploadResponse>,
//                response: Response<ActivityResponses.SignUpUploadResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null && !responseBody.error) {
//                        Toast.makeText(this@SignUpActivity, responseBody.message, Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this@SignUpActivity, response.message(), Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onFailure(call: Call<ActivityResponses.SignUpUploadResponse>, t: Throwable) {
//                Toast.makeText(this@SignUpActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
}