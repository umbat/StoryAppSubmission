package com.umbat.storyappsubmission.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
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
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): Call<ActivityResponses.FileUploadResponse>

    @GET("v1/stories")
    suspend fun getStoriesList(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int = 0
    ): ActivityResponses.GetAllStoriesResponse

    @GET("v1/stories")
    fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") includeLocation: Int = 1
    ): Call<ActivityResponses.GetAllStoriesResponse>

}