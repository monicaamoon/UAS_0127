package com.example.uas_0127.utils

interface ServersCallback {
    fun onSuccess(response: String)

    fun onFailed(response: String)

    fun onFailure(throwable: Throwable)
}