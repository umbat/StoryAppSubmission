package com.umbat.storyappsubmission.view.main.home

import androidx.lifecycle.*
import com.umbat.storyappsubmission.api.ActivityResponses
import com.umbat.storyappsubmission.api.ApiConfig
import com.umbat.storyappsubmission.model.*
import com.umbat.storyappsubmission.repo.Repository
import kotlinx.coroutines.launch

class HomeViewModel(private val pref: Repository) : ViewModel() {
    val getAllStoriesResponse: LiveData<ActivityResponses.GetAllStoriesResponse> = pref.getAllStoriesResponse
    val showLoading: LiveData<Boolean> = pref.showLoading
    val toastText: LiveData<String> = pref.toastText

    fun getStoriesList(token: String) {
        viewModelScope.launch {
            pref.getStoriesList(token)
        }
    }

    fun loadState(): LiveData<TokenModel> {
        return pref.loadState()
    }
}