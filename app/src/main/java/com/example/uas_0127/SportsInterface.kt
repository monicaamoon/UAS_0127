package com.example.uas_0127

import android.app.Activity
import android.content.Context
import com.example.uas_0127.EventItem
import com.example.uas_0127.utils.ServersCallback

interface SportsInterface {
    fun isNetworkAvailable(context: Activity): Boolean
    fun getSpinnerData(context: Activity,  callback: ServersCallback)
    fun getNextMatch(context: Activity, id : String,  callback: ServersCallback)
    fun isSuccess(response: String): Boolean
    fun parsingData(context: Activity, response: String): ArrayList<EventItem>
    fun getFavoritesAll(context: Context) : ArrayList<EventItem>
}