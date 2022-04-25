package com.umbat.storyappsubmission.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryModel(
    val name: String,
    val photo: String? = null,
    val id: String,
    val description: String? = null
) : Parcelable