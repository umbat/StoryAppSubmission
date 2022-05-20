package com.umbat.storyappsubmission.ui.main.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbat.storyappsubmission.model.TokenModel
import com.umbat.storyappsubmission.repo.Repository
import kotlinx.coroutines.launch

class MapsViewModel(private val pref: Repository) : ViewModel(){
    val showLoading get() = pref.showLoading
    val getAllStoriesResponse get() = pref.getAllStoriesResponse

    fun getStoriesLocation(token: String) {
        viewModelScope.launch {
            pref.getStoriesLocation(token)
        }
    }

    fun loadState(): LiveData<TokenModel> {
        return pref.loadState()
    }
}