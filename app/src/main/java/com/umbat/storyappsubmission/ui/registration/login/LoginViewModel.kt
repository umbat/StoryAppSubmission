package com.umbat.storyappsubmission.ui.registration.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbat.storyappsubmission.api.ActivityResponses
import com.umbat.storyappsubmission.repo.Repository
import com.umbat.storyappsubmission.model.TokenModel
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: Repository) : ViewModel() {
    val loginResult: MutableLiveData<ActivityResponses.LoginResult?> = pref.loginResult
    val loginResponse: LiveData<ActivityResponses.LoginResponse> = pref.loginResponse
    val showLoading: LiveData<Boolean> = pref.showLoading
    val toastText: LiveData<String> = pref.toastText

    fun loginAccount(email: String, password: String) {
        viewModelScope.launch {
            pref.loginAccount(email, password)
        }
    }

    fun saveState(token: TokenModel){
        viewModelScope.launch {
            pref.saveState(token)
        }
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }
}