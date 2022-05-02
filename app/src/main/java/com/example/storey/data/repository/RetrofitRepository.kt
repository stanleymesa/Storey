package com.example.storey.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.local.model.LoginModel
import com.example.storey.data.local.model.RegisterModel
import com.example.storey.data.local.model.UploadModel
import com.example.storey.data.paging.StoryRemoteMediator
import com.example.storey.data.remote.api.ApiConfig
import com.example.storey.data.remote.api.ApiServices
import com.example.storey.data.remote.response.*

class RetrofitRepository {
    var apiServices = ApiConfig.getApiServices()

    suspend fun register(registerModel: RegisterModel): RegisterResponse? {
        try {
            val request = apiServices.register(
                name = registerModel.name,
                email = registerModel.email,
                password = registerModel.password
            )
            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return RegisterResponse(error = true, message = request.code().toString())

        } catch (ex: Exception) {
        }
        return null
    }

    suspend fun login(loginModel: LoginModel): LoginResponse? {
        try {
            val request = apiServices.login(
                email = loginModel.email,
                password = loginModel.password
            )
            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return LoginResponse(error = true, message = request.code().toString())

        } catch (ex: Exception) {
        }
        return null
    }

    fun getAllStories(
        token: String,
        apiServices: ApiServices,
        database: MainDatabase,
    ): LiveData<PagingData<ListStoryItem>> {

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(
                token = token,
                apiServices = apiServices,
                database = database),
            pagingSourceFactory = {
                database.listStoryitemDao().getAllListStory()
            }
        ).liveData

    }

    suspend fun getStoriesWidget(token: String, size: Int): GetAllStoriesResponse? {
        try {
            val request = apiServices.getAllStories(
                token = token,
                size = size
            )
            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return GetAllStoriesResponse(error = true, message = request.code().toString())

        } catch (ex: Exception) {
        }
        return null
    }

    suspend fun uploadStory(uploadModel: UploadModel): FileUploadResponse? {
        try {
            val request = apiServices.uploadStory(
                token = uploadModel.token,
                file = uploadModel.file,
                description = uploadModel.description,
                lat = uploadModel.lat,
                lon = uploadModel.lon
            )
            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return FileUploadResponse(error = true, message = request.message())
        } catch (ex: java.lang.Exception) {
        }
        return null
    }

}