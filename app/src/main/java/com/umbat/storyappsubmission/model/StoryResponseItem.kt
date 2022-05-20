package com.umbat.storyappsubmission.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryResponseItem(
    val name: String,
    val photoUrl: String? = null,
    val id: String,
    val description: String? = null,
    val lat: Double,
    val lon: Double
) : Parcelable