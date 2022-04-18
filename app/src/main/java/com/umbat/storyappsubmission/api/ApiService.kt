package com.umbat.storyappsubmission.api

import androidx.viewbinding.BuildConfig
import com.google.gson.annotations.SerializedName
import com.umbat.storyappsubmission.model.UserModel
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class ApiService {
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

    data class FileUploadResponse(
        @field:SerializedName("error")
        val error: Boolean,

        @field:SerializedName("message")
        val message: String
    )

    interface ApiService {
        @POST("v1/register")
        fun registerAccount(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("password") password: String
        ): Call<FileUploadResponse>

        @POST("v1/login")
        fun loginAccount(
            @Field("email") email: String,
            @Field("password") password: String
        ):Call<FileUploadResponse>

        @Multipart
        @POST("v1/stories")
        @Headers("Authorization: Bearer <token>")
        fun uploadImage(
            @Part file: MultipartBody.Part,
            @Part("description") description: RequestBody,
        ): Call<FileUploadResponse>

        @GET("v1/stories")
        @Headers("Authorization: Bearer <token>")
        fun getStoriesList(
            @Query("q") query: String
        ): Call<FileUploadResponse>
    }
}
