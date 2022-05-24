package com.umbat.storyappsubmission.ui.registration.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbat.storyappsubmission.api.ActivityResponses
import com.umbat.storyappsubmission.repo.Repository
import kotlinx.coroutines.launch

class SignUpViewModel(private val pref: Repository) : ViewModel() {
    val signUpResponse: LiveData<ActivityResponses.SignUpResponse> = pref.signUpResponse
    val toastText: LiveData<String> = pref.toastText
    val showLoading: LiveData<Boolean> = pref.showLoading

    fun registerAccount(name: String, email: String, password: String) {
        viewModelScope.launch {
            pref.registerAccount(name, email, password)
        }
    }
}