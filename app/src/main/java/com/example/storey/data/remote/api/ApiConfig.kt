package com.example.storey.data.remote.api

import com.example.storey.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ApiConfig {

    companion object {

        var BASE_URL = BuildConfig.BASE_URL

        fun getApiServices(): ApiServices {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiServices::class.java)
        }
    }
}