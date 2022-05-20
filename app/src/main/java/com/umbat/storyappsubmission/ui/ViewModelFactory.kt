package com.umbat.storyappsubmission.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umbat.storyappsubmission.repo.Injection
import com.umbat.storyappsubmission.repo.Repository
import com.umbat.storyappsubmission.ui.main.addstory.AddStoryViewModel
import com.umbat.storyappsubmission.ui.main.home.HomeViewModel
import com.umbat.storyappsubmission.ui.main.logout.LogoutViewModel
import com.umbat.storyappsubmission.ui.main.maps.MapsViewModel
import com.umbat.storyappsubmission.ui.registration.login.LoginViewModel
import com.umbat.storyappsubmission.ui.registration.signup.SignUpViewModel

class ViewModelFactory(private val pref: Repository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(pref) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LogoutViewModel::class.java) -> {
                LogoutViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}