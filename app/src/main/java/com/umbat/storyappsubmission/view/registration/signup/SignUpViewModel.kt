package com.umbat.storyappsubmission.view.registration.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbat.storyappsubmission.model.UserModel
import com.umbat.storyappsubmission.model.UserPreference
import kotlinx.coroutines.launch

class SignUpViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}