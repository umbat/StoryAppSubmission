package com.umbat.storyappsubmission.view.main.home

import androidx.lifecycle.*
import com.umbat.storyappsubmission.model.*
import com.umbat.storyappsubmission.repo.Repository
import kotlinx.coroutines.launch

class HomeViewModel(private val pref: Repository) : ViewModel() {
    val getAllStoriesResponse get() = pref.getAllStoriesResponse
    val showLoading get() = pref.showLoading
    val toastText get() = pref.toastText

    fun getStoriesList(token: String) {
        viewModelScope.launch {
            pref.getStoriesList(token)
        }
    }

    fun loadState(): LiveData<TokenModel> {
        return pref.loadState()
    }
}