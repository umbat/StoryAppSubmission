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

class ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}

interface ApiService {
    @POST("v1/register")
    fun registerAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ActivityResponses.SignUpUploadResponse>

    @POST("v1/login")
    fun loginAccount(
        @Field("email") name: String,
        @Field("password") email: String,
        @Field("token") password: String
    ):Call<ActivityResponses.LoginUploadResponse>

    @Multipart
    @POST("v1/stories")
    @Headers("Authorization: Bearer <token>")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<ActivityResponses.FileUploadResponse>

    @GET("v1/stories")
    fun getStoriesList(
        @Header("Authorization") Authorization: String
    ): Call<ActivityResponses.GetAllStoriesResponse>
}