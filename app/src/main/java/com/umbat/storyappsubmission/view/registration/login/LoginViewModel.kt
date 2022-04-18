package com.umbat.storyappsubmission.view.registration.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.umbat.storyappsubmission.model.TokenModel
import com.umbat.storyappsubmission.model.UserModel
import com.umbat.storyappsubmission.model.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }

    fun getToken(): LiveData<TokenModel> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: TokenModel){
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }
}