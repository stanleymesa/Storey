package com.example.storey.data.remote.api

import com.example.storey.data.remote.response.FileUploadResponse
import com.example.storey.data.remote.response.GetAllStoriesResponse
import com.example.storey.data.remote.response.LoginResponse
import com.example.storey.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): Response<GetAllStoriesResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: String,
        @Part("lat") lat: Float? = null,
        @Part("lon") lon: Float? = null,
    ): Response<FileUploadResponse>
}