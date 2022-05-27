package com.umbat.storyappsubmission.ui.main.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbat.storyappsubmission.api.ActivityResponses
import com.umbat.storyappsubmission.model.TokenModel
import com.umbat.storyappsubmission.repo.Repository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val pref: Repository) : ViewModel() {
    val fileUploadResponse: LiveData<ActivityResponses.FileUploadResponse> = pref.fileUploadResponse
    val showLoading: LiveData<Boolean> = pref.showLoading
    val toastText: LiveData<String> = pref.toastText

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?) {
        viewModelScope.launch {
            pref.uploadStory(token, file, description, lat, lon)
        }
    }

    fun loadState(): LiveData<TokenModel> {
        return pref.loadState()
    }
}