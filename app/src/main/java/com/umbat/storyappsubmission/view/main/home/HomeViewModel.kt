package com.umbat.storyappsubmission.view.main.home

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.umbat.storyappsubmission.api.ActivityResponses
import com.umbat.storyappsubmission.api.ApiConfig
import com.umbat.storyappsubmission.model.StoryModel
import com.umbat.storyappsubmission.model.TokenModel
import com.umbat.storyappsubmission.model.UserModel
import com.umbat.storyappsubmission.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.prefs.Preferences

class HomeViewModel(private val pref: UserPreference) : ViewModel() {
    private val listStories = MutableLiveData<ArrayList<StoryModel>>()

    fun setStoriesList(token: String) {
        ApiConfig().getApiService()
            .getStoriesList(token)
            .enqueue(object: Callback<ActivityResponses.GetAllStoriesResponse>{
                override fun onResponse(
                    call: Call<ActivityResponses.GetAllStoriesResponse>,
                    response: Response<ActivityResponses.GetAllStoriesResponse>
                ) {
                    if (response.isSuccessful){
                        listStories
                    }
                }

                override fun onFailure(call: Call<ActivityResponses.GetAllStoriesResponse>, t: Throwable) {
                    Log.d("Failed", t.message.toString())
                }

            })
    }

    fun getStoriesList(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}