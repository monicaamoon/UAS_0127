package com.example.uas_0127.detail

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.net.ConnectivityManager
import android.util.Log
import com.example.uas_0127.ApiService
import com.example.uas_0127.ApiUtils
import com.example.uas_0127.EventItem
import com.example.uas_0127.utils.ServersCallback
import com.example.uas_0127.utils.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class DetailPresenters : DetailInterface {
    override fun addFavorites(context: Context, data: EventItem) {
        try {
            context.database.use {
                insert(
                    EventItem.TABLE_FAVORITES,
                    EventItem.ID_EVENT to data.idEvent,
                    EventItem.DATE to data.dateEvent,
                    EventItem.STR_SPORT to data.strSport,


                    EventItem.HOME_ID to data.idHomeTeam,
                    EventItem.HOME_TEAM to data.strHomeTeam,
                    EventItem.HOME_SCORE to data.intHomeScore,
                    EventItem.HOME_FORMATION to data.strHomeFormation,
                    EventItem.HOME_GOAL_DETAILS to data.strHomeGoalDetails,
                    EventItem.HOME_SHOTS to data.intHomeShots,
                    EventItem.HOME_LINEUP_GOALKEEPER to data.strHomeLineupGoalkeeper,
                    EventItem.HOME_LINEUP_DEFENSE to data.strHomeLineupDefense,
                    EventItem.HOME_LINEUP_MIDFIELD to data.strHomeLineupMidfield,
                    EventItem.HOME_LINEUP_FORWARD to data.strHomeLineupForward,
                    EventItem.HOME_LINEUP_SUBSTITUTES to data.strHomeLineupSubstitutes,


                    EventItem.AWAY_ID to data.idAwayTeam,
                    EventItem.AWAY_TEAM to data.strAwayTeam,
                    EventItem.AWAY_SCORE to data.intAwayScore,
                    EventItem.AWAY_FORMATION to data.strAwayFormation,
                    EventItem.AWAY_GOAL_DETAILS to data.strAwayGoalDetails,
                    EventItem.AWAY_SHOTS to data.intAwayShots,
                    EventItem.AWAY_LINEUP_GOALKEEPER to data.strAwayLineupGoalkeeper,
                    EventItem.AWAY_LINEUP_DEFENSE to data.strAwayLineupDefense,
                    EventItem.AWAY_LINEUP_MIDFIELD to data.strAwayLineupMidfield,
                    EventItem.AWAY_LINEUP_FORWARD to data.strAwayLineupForward,
                    EventItem.AWAY_LINEUP_SUBSTITUTES to data.strAwayLineupSubstitutes)
            }
        } catch (e: SQLiteConstraintException) {
            context.toast("Error: ${e.message}")
        }
    }

    override fun removeFavorites(context: Context, data: EventItem) {
        try {
            context.database.use {
                delete(
                    EventItem.TABLE_FAVORITES,
                    EventItem.ID_EVENT + " = {id}",
                    "id" to data.idEvent.toString())
            }
        } catch (e: SQLiteConstraintException) {
            context.toast("Error: ${e.message}")
        }
    }

    override fun isFavorite(context: Context, data: EventItem): Boolean {
        var bFavorite = false

        context.database.use {
            val favorites = select(EventItem.TABLE_FAVORITES)
                .whereArgs(
                    EventItem.ID_EVENT + " = {id}",
                    "id" to data.idEvent.toString())
                .parseList(classParser<EventItem>())

            bFavorite = !favorites.isEmpty()
        }

        return bFavorite
    }

    override fun isNetworkAvailable(context: Activity): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun geDetailTeam(context: Activity, id: String, callback: ServersCallback) {
        val mApiService: ApiService = ApiUtils.apiService
        CoroutineScope(Dispatchers.Main).launch {
            async {
                mApiService.    getLookupteam(id)
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
                val golDetailHome = data.getString("intHomeScore")
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
                val golDetailAway = data.getString("intAwayScore")
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