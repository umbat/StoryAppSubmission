package com.umbat.storyappsubmission.view.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umbat.storyappsubmission.api.ApiService
import com.umbat.storyappsubmission.model.StoryModel
import com.umbat.storyappsubmission.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    val listStories= MutableLiveData<ArrayList<StoryModel>>()

    fun getStoriesList(): LiveData<ArrayList<StoryModel>>{
        return listStories
    }

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text
}