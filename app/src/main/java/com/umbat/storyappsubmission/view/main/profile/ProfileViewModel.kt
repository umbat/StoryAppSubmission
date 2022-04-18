package com.umbat.storyappsubmission.view.main.profile

import androidx.lifecycle.*
import com.umbat.storyappsubmission.model.UserModel
import com.umbat.storyappsubmission.model.UserPreference
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}