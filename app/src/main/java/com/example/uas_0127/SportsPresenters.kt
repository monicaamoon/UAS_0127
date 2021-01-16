package com.example.uas_0127

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.uas_0127.ApiService
import com.example.uas_0127.ApiUtils
import com.example.uas_0127.EventItem
import com.example.uas_0127.utils.ServersCallback
import com.example.uas_0127.utils.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import kotlinx.coroutines.async
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SportsPresenters (private val view: SportsView) : SportsInterface{
    override fun getFavoritesAll(context: Context): ArrayList<EventItem> {
        view.showLoading()
        val data: ArrayList<EventItem> = ArrayList()

        context.database.use {
            val favorites = select(EventItem.TABLE_FAVORITES)
                .parseList(classParser<EventItem>())
            data.addAll(favorites)
        }

        view.hideLoading()
        return data
    }


    override fun isNetworkAvailable(context: Activity): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    override fun parsingData(context: Activity, response: String): ArrayList<EventItem> {
        val listData: ArrayList<EventItem> = ArrayList()
        try {
            val jsonObject = JSONObject(response)
            Log.d("TAG", "Response error exception $jsonObject")
            val message = jsonObject.getJSONArray("events")
            for (i in 0 until message.length()) {
                val data = message.getJSONObject(i)
                val idEvent = data.getString("idEvent")
                val dateEvent = data.getString("dateEvent")
                val strSport = data.getString("strSport")

                val idHome = data.getString("idHomeTeam")
                val nameHome = data.getString("strHomeTeam")
                val scoreHome = data.getString("intHomeScore")
                val formationHome = data.getString("strHomeFormation")
                val golDetailHome = data.getString("strHomeGoalDetails")
                val intShotHome = data.getString("intHomeShots")
                val lineupgkHome = data.getString("strHomeLineupGoalkeeper")
                val lineupdefHome = data.getString("strHomeLineupDefense")
                val lineupmidhome = data.getString("strHomeLineupMidfield")
                val lineupforwadHome = data.getString("strHomeLineupForward")
                val lineupsubHome = data.getString("strHomeLineupSubstitutes")

                val idAway = data.getString("idAwayTeam")
                val nameAway = data.getString("strAwayTeam")
                val scoreAway = data.getString("intAwayScore")
                val formationAway = data.getString("strAwayFormation")
                val golDetailAway = data.getString("strAwayGoalDetails")
                val intShotAway = data.getString("intAwayShots")
                val lineupgkAway = data.getString("strAwayLineupGoalkeeper")
                val lineupdefAway = data.getString("strAwayLineupDefense")
                val lineupmidAway = data.getString("strAwayLineupMidfield")
                val lineupforwadAway = data.getString("strAwayLineupForward")
                val lineupsubAway = data.getString("strAwayLineupSubstitutes")

                listData.add(
                    EventItem(i.toLong(), idEvent, dateEvent, strSport, idHome, nameHome, scoreHome, formationHome, golDetailHome, intShotHome, lineupgkHome, lineupdefHome, lineupmidhome, lineupforwadHome, lineupsubHome,
                        idAway, nameAway, scoreAway, formationAway, golDetailAway, intShotAway, lineupgkAway, lineupdefAway, lineupmidAway, lineupforwadAway, lineupsubAway
                    )
                )
            }
        } catch (e: Exception) {
            Log.d("TAG", "Response error exception $e")
        }
        return listData
    }
    fun getPrevMatch(context: Activity, id: String, callback: ServersCallback) {
        view.showLoading()
        val mApiService: ApiService = ApiUtils.apiService
        CoroutineScope(Dispatchers.Main).launch {
            async {
                mApiService.geteventsPasteague(id)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                callback.onSuccess(response.body()!!.string())
                            } else {
                                callback.onFailed(response.errorBody()!!.toString())
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            callback.onFailure(t)
                        }
                    })
            }
        }
        view.hideLoading()
    }
    override fun getNextMatch(context: Activity, id: String, callback: ServersCallback) {
        view.showLoading()
        val mApiService: ApiService = ApiUtils.apiService
        CoroutineScope(Dispatchers.Main).launch {
            async {
                mApiService.geteeventsNextleague(id)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                callback.onSuccess(response.body()!!.string())
                            } else {
                                callback.onFailed(response.errorBody()!!.toString())
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            callback.onFailure(t)
                        }
                    })
            }
        }
        view.hideLoading()
    }


    override fun getSpinnerData(context: Activity, callback: ServersCallback) {
        val mApiService: ApiService = ApiUtils.apiService
        CoroutineScope(Dispatchers.Main).launch {
            async {
                mApiService.getallLeagues()
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                callback.onSuccess(response.body()!!.string())
                            } else {
                                callback.onFailed(response.errorBody()!!.toString())
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            callback.onFailure(t)
                        }
                    })
            }
        }
    }

    override fun isSuccess(response: String): Boolean {
        var success = true
        try {
            val jObj = JSONObject(response)
            success = jObj.getBoolean("success")
        } catch (e: Exception) {

        }
        return success
    }

}
