package com.example.storey

import com.example.storey.data.local.database.RemoteKeys
import com.example.storey.data.local.database.StoryWidget
import com.example.storey.data.local.model.LoginModel
import com.example.storey.data.local.model.RegisterModel
import com.example.storey.data.local.model.UploadModel
import com.example.storey.data.remote.response.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DummyData {

    fun getDummyUploadModel(): UploadModel {
        val dummyFile: RequestBody = "file"
            .toRequestBody("image/jpeg".toMediaType())

        return UploadModel(
            token = "123456",
            file = MultipartBody.Part.createFormData(
                "photo",
                "photo.jpg",
                dummyFile
            ),
            description = "description",
            lat = 0F,
            lon = 0F
        )
    }

    fun getDummyUploadResponseSuccess(): FileUploadResponse {
        return FileUploadResponse(
            error = false,
            message = "Upload Success"
        )
    }

    fun getDummyUploadResponseFailed(): FileUploadResponse {
        return FileUploadResponse(
            error = true,
            message = "Bad Request"
        )
    }

    fun getDummyRegisterModel(): RegisterModel {
        return RegisterModel(
            name = "budi",
            email = "halo@gmail.com",
            password = "qqqqqq"
        )
    }

    fun getDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "Register Success"
        )
    }

    fun getDummyLoginModel(): LoginModel {
        return LoginModel(
            email = "halo@gmail.com",
            password = "qqqqqq"
        )
    }

    fun getDummyLoginResponseSuccess(): LoginResponse {
        val loginResult = LoginResult(
            name = "budi",
            userId = "abcdefg",
            token = "123456"
        )

        return LoginResponse(
            error = false,
            message = "Login Success",
            loginResult = loginResult
        )
    }

    fun getDummyLoginResponseFailed(): LoginResponse {

        return LoginResponse(
            error = true,
            message = "Bad Request",
        )
    }

    fun getDummyStoryResponseSuccess(): GetAllStoriesResponse {
        val listStoryItem = ListStoryItem(
            photoUrl = "https://photo.com",
            createdAt = "28-07-2022",
            name = "budi",
            description = "deskripsi",
            id = "abcdefg",
            lat = 0F,
            lon = 0F
        )

        return GetAllStoriesResponse(
            error = false,
            message = "Success",
            listStory = listOf(listStoryItem)
        )
    }

    fun getDummyStoryResponseFailed(): GetAllStoriesResponse {

        return GetAllStoriesResponse(
            error = true,
            message = "Success",
        )
    }

    fun getDummyStoryItems(): List<ListStoryItem> {
        val list = arrayListOf<ListStoryItem>()

        for (i in 1..10) {
            list.add(
                ListStoryItem(
                    photoUrl = "https://photo.com",
                    createdAt = "28-07-2022",
                    name = "budi",
                    description = "deskripsi",
                    id = "abcdefg",
                    lat = 0F,
                    lon = 0F
                )
            )
        }

        return list
    }

    fun getDummyStoryWidget(): StoryWidget {
        return StoryWidget(
            id = 1,
            photoUrl = "https://photo.com"
        )
    }

    fun getDummyRemoteKeys(): List<RemoteKeys> {
        return arrayListOf(
            RemoteKeys(
                id = "asdkjasdl",
                prevKey = 1,
                nextKey = 3
            )
        )
    }
}