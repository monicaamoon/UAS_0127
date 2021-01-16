package com.example.uas_0127

object ApiUtils {
    val apiService: ApiService
        get() = RetrofitClient.getClient(ApiConfig.END_POINT).create(ApiService::class.java)
}