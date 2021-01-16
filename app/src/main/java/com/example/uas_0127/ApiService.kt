package com.example.uas_0127

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET(ApiConfig.allLeagues)
    fun getallLeagues(): Call<ResponseBody>

    @GET(ApiConfig.eventsNextleague)
    fun geteeventsNextleague(@Query("id") id : String): Call<ResponseBody>

    @GET(ApiConfig.eventsPastleague)
    fun geteventsPasteague(@Query("id") id : String): Call<ResponseBody>


    @GET(ApiConfig.lookupTeam)
    fun getLookupteam(@Query("id") id : String): Call<ResponseBody>


}