package com.umbat.storyappsubmission.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umbat.storyappsubmission.model.UserPreference
import com.umbat.storyappsubmission.view.main.profile.ProfileViewModel
import com.umbat.storyappsubmission.view.registration.login.LoginViewModel
import com.umbat.storyappsubmission.view.registration.signup.SignUpViewModel

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}