package com.example.storey.data.local.model

import okhttp3.MultipartBody

data class UploadModel(
    val token: String,
    val file: MultipartBody.Part,
    val description: String,
    val lat: Float? = null,
    val lon: Float? = null,
)
