package com.umbat.storyappsubmission.api

import com.google.gson.annotations.SerializedName
import com.umbat.storyappsubmission.model.StoryResponseItem

class ActivityResponses {
    data class FileUploadResponse(
        @field:SerializedName("error")
        val error: Boolean,

        @field:SerializedName("message")
        val message: String
    )

    data class SignUpResponse(
        @field:SerializedName("error")
        val error: Boolean,

        @field:SerializedName("message")
        val message: String
    )

    data class LoginResponse(
        @field:SerializedName("error")
        val error: Boolean,

        @field:SerializedName("message")
        val message: String,

        @field:SerializedName("loginResult")
        val loginResult: LoginResult? = null
    )

    data class LoginResult(
        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("token")
        val token: String
    )

    data class GetAllStoriesResponse(
        @field:SerializedName("error")
        val error: Boolean,

        @field:SerializedName("message")
        val message: String,

        @field:SerializedName("listStory")
        val listStory: List<StoryResponseItem>
    )
}