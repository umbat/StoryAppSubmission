package com.umbat.storyappsubmission.api

import com.google.gson.annotations.SerializedName
import com.umbat.storyappsubmission.model.UserModel

class ActivityResponses {
    data class FileUploadResponse(
        @field:SerializedName("error")
        val error: Boolean,

        @field:SerializedName("message")
        val message: String
    )

    data class SignUpUploadResponse(
        @field:SerializedName("error")
        val error: Boolean,

        @field:SerializedName("message")
        val message: String
    )

    data class LoginUploadResponse(
        @field:SerializedName("error")
        val error: Boolean,

        @field:SerializedName("message")
        val message: String,

        @field:SerializedName("loginResult")
        val loginResult: String
    )

    data class GetAllStoriesResponse(
        @field:SerializedName("error")
        val error: Boolean,

        @field:SerializedName("message")
        val message: String,

        @field:SerializedName("listStory")
        val listStory: ArrayList<UserModel>
    )
}