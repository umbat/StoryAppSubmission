package com.umbat.storyappsubmission.api

import androidx.viewbinding.BuildConfig
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("v1/register")
    fun registerAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ActivityResponses.SignUpResponse>

    @FormUrlEncoded
    @POST("v1/login")
    fun loginAccount(
        @Field("email") name: String,
        @Field("password") email: String
    ): Call<ActivityResponses.LoginResponse>

    @Multipart
    @POST("v1/stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<ActivityResponses.FileUploadResponse>

    @GET("v1/stories")
    fun getStoriesList(
        @Header("Authorization") token: String
    ): Call<ActivityResponses.GetAllStoriesResponse>
}