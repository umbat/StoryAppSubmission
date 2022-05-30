package com.umbat.storyappsubmission.ui.main.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.umbat.storyappsubmission.model.*
import com.umbat.storyappsubmission.repo.Repository
import kotlinx.coroutines.launch

class HomeViewModel(private val pref: Repository) : ViewModel() {
    val getAllStoriesResponse get() = pref.getAllStoriesResponse
    val showLoading get() = pref.showLoading
    val toastText get() = pref.toastText

    fun getStoriesList(token: String): LiveData<PagingData<StoryResponseItem>> {
        viewModelScope.launch {
            pref.getStoriesList(token)
        }
        return pref.getStoriesList(token).cachedIn(viewModelScope)
    }

    fun loadState(): LiveData<TokenModel> {
        return pref.loadState()
    }
}