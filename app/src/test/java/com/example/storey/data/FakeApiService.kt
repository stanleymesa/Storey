package com.example.storey.data

import com.example.storey.DummyData
import com.example.storey.data.remote.api.ApiServices
import com.example.storey.data.remote.response.FileUploadResponse
import com.example.storey.data.remote.response.GetAllStoriesResponse
import com.example.storey.data.remote.response.LoginResponse
import com.example.storey.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Response

class FakeApiService : ApiServices {
    override suspend fun register(
        name: String,
        email: String,
        password: String,
    ): Response<RegisterResponse> {
        return Response.success(DummyData.getDummyRegisterResponse())
    }

    override suspend fun login(email: String, password: String): Response<LoginResponse> {
        return Response.success(DummyData.getDummyLoginResponseSuccess())
    }

    override suspend fun getAllStories(
        token: String,
        page: Int,
        size: Int,
    ): Response<GetAllStoriesResponse> {
        return Response.success(DummyData.getDummyStoryResponseSuccess())
    }

    override suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: String,
        lat: Float?,
        lon: Float?,
    ): Response<FileUploadResponse> {
        return Response.success(DummyData.getDummyUploadResponseSuccess())
    }
}