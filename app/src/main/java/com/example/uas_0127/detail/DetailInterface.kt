package com.example.uas_0127.detail

import android.app.Activity
import android.content.Context
import com.example.uas_0127.EventItem
import com.example.uas_0127.utils.ServersCallback

interface DetailInterface {
    fun isNetworkAvailable(context: Activity): Boolean
    fun geDetailTeam(context: Activity, id : String,  callback: ServersCallback)
    fun isSuccess(response: String): Boolean
    fun parsingData(context: Activity, response: String): ArrayList<EventItem>
    fun addFavorites(context: Context, data: EventItem)
    fun removeFavorites(context: Context, data: EventItem)
    fun isFavorite(context: Context, data: EventItem): Boolean
}