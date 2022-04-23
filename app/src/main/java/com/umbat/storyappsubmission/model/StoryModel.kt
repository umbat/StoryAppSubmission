package com.umbat.storyappsubmission.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryModel(
    val name: String,
    val id: String,
    val description: String? = null,
    val photo: String? = null
)  : Parcelable