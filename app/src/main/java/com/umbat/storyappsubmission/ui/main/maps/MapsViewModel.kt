package com.umbat.storyappsubmission.ui.main.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbat.storyappsubmission.model.TokenModel
import com.umbat.storyappsubmission.repo.Repository
import kotlinx.coroutines.launch

class MapsViewModel(private val pref: Repository) : ViewModel() {
    val getAllStoriesResponse get() = pref.getAllStoriesResponse
    val showLoading get() = pref.showLoading
    val toastText get() = pref.toastText

    fun getStoriesWithLocation(token: String) {
        viewModelScope.launch {
            pref.getStoriesWithLocation(token)
        }
    }

    fun loadState(): LiveData<TokenModel> {
        return pref.loadState()
    }
}