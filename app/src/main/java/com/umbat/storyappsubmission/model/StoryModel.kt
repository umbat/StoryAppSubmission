package com.umbat.storyappsubmission.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryModel(
    val name: String,
    val photoUrl: String? = null,
    val id: String,
    val description: String? = null,
    val lon: Double,
    val lat: Double
) : Parcelable