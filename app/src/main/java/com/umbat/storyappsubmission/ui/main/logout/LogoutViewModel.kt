package com.umbat.storyappsubmission.ui.main.logout

import androidx.lifecycle.*
import com.umbat.storyappsubmission.repo.Repository
import kotlinx.coroutines.launch

class LogoutViewModel(private val pref: Repository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}