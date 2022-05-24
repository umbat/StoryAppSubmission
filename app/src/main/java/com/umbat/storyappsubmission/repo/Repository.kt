package com.umbat.storyappsubmission.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.umbat.storyappsubmission.api.ActivityResponses
import com.umbat.storyappsubmission.api.ApiService
import com.umbat.storyappsubmission.model.TokenModel
import com.umbat.storyappsubmission.model.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository private constructor(
    private val pref: UserPreference,
    private val apiService: ApiService
) {
    private val _signUpResponse = MutableLiveData<ActivityResponses.SignUpResponse>()
    val signUpResponse: LiveData<ActivityResponses.SignUpResponse> = _signUpResponse

    private val _loginResponse = MutableLiveData<ActivityResponses.LoginResponse>()
    val loginResponse: LiveData<ActivityResponses.LoginResponse> = _loginResponse

    private val _loginResult = MutableLiveData<ActivityResponses.LoginResult?>()
    val loginResult: MutableLiveData<ActivityResponses.LoginResult?> = _loginResult

    private val _fileUploadResponse = MutableLiveData<ActivityResponses.FileUploadResponse>()
    val fileUploadResponse: LiveData<ActivityResponses.FileUploadResponse> = _fileUploadResponse

    private val _getAllStoriesResponse = MutableLiveData<ActivityResponses.GetAllStoriesResponse>()
    val getAllStoriesResponse: LiveData<ActivityResponses.GetAllStoriesResponse> = _getAllStoriesResponse

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _toastText = MutableLiveData<String>()
    val toastText: LiveData<String> = _toastText

    fun registerAccount(name: String, email: String, password: String) {
        _showLoading.value = true
        val client = apiService.registerAccount(name, email, password)

        client.enqueue(object : Callback<ActivityResponses.SignUpResponse> {
            override fun onResponse(
                call: Call<ActivityResponses.SignUpResponse>,
                response: Response<ActivityResponses.SignUpResponse>
            ) {
                _showLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _signUpResponse.value = response.body()
                    _toastText.value = response.body()?.message
                } else {
                    _toastText.value = response.message().toString()
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<ActivityResponses.SignUpResponse>, t: Throwable) {
                _toastText.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun loginAccount(email: String, password: String) {
        _showLoading.value = true
        val client = apiService.loginAccount(email, password)

        client.enqueue(object : Callback<ActivityResponses.LoginResponse> {
            override fun onResponse(
                call: Call<ActivityResponses.LoginResponse>,
                response: Response<ActivityResponses.LoginResponse>
            ) {
                _showLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _loginResponse.value = response.body()
                    _toastText.value = response.body()?.message
                    _loginResult.value = response.body()?.loginResult
                } else {
                    _toastText.value = response.message().toString()
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<ActivityResponses.LoginResponse>, t: Throwable) {
                _toastText.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        _showLoading.value = true
        val client = apiService.uploadImage(token, file, description)

        client.enqueue(object : Callback<ActivityResponses.FileUploadResponse> {
            override fun onResponse(
                call: Call<ActivityResponses.FileUploadResponse>,
                response: Response<ActivityResponses.FileUploadResponse>
            ) {
                _showLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _fileUploadResponse.value = response.body()
                    _toastText.value = response.body()?.message
                } else {
                    _toastText.value = response.message().toString()
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<ActivityResponses.FileUploadResponse>, t: Throwable) {
                Log.d("error upload", t.message.toString())
            }

        }
        )
    }

    fun getStoriesList(token: String) {
        _showLoading.value = true
        val client = apiService.getStoriesList(token)
        Log.d("TOKEN", token)

        client.enqueue(object : Callback<ActivityResponses.GetAllStoriesResponse> {
            override fun onResponse(
                call: Call<ActivityResponses.GetAllStoriesResponse>,
                response: Response<ActivityResponses.GetAllStoriesResponse>
            ) {
                _showLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _getAllStoriesResponse.value = response.body()
                } else {
                    _toastText.value = response.message().toString()
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<ActivityResponses.GetAllStoriesResponse>, t: Throwable) {
                _toastText.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getStoriesWithLocation(token: String) {
        _showLoading.value = true
        val client = apiService.getStoriesWithLocation(token)
        Log.d("TOKEN", token)

        client.enqueue(object : Callback<ActivityResponses.GetAllStoriesResponse> {
            override fun onResponse(
                call: Call<ActivityResponses.GetAllStoriesResponse>,
                response: Response<ActivityResponses.GetAllStoriesResponse>
            ) {
                _showLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _getAllStoriesResponse.value = response.body()
                } else {
                    _toastText.value = response.message().toString()
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<ActivityResponses.GetAllStoriesResponse>, t: Throwable) {
                _toastText.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun loadState(): LiveData<TokenModel> {
        return pref.loadState().asLiveData()
    }

    suspend fun saveState(session: TokenModel) {
        pref.saveState(session)
    }

    suspend fun login() {
        pref.login()
    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        private const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            preferences: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(preferences, apiService)
            }.also { instance = it }
    }
}