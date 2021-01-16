package com.example.uas_0127.detail

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.example.uas_0127.R
import com.example.uas_0127.EventItem
import com.example.uas_0127.R.id.*
import com.example.uas_0127.utils.ServersCallback
import com.example.uas_0127.utils.getLongDate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.appbar_main.*
import org.jetbrains.anko.ctx
import org.json.JSONObject

const val INTENT_DETAIL = "INTENT_DETAIL"

class DetailActivity : AppCompatActivity() {


    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification.Builder
    private val channelId = "12345"
    private val description = "Berhasil menambahkan"
    private var badgeHome: String? = ""
    private var badgeAway: String? = ""
    private var idHome: String? = ""
    private var idAway: String? = ""
    private var idEvent = ""
    private var idTeam = arrayOf<String>()
    private var menuItem: String = ""
    private val presenter = DetailPresenters()
    private var menuFavorites: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var data: EventItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        implementPutExtra()
        setDetail()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager


    }

    private fun initLayout() {
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar_main)
        menuItem = intent.getStringExtra(getString(R.string.menu_item)).toString()
        data = intent.getParcelableExtra(INTENT_DETAIL)!!
        if (menuItem.toInt() == 1) {
            tv_detail_no_result.visibility = View.INVISIBLE
            layout_scores.visibility = View.VISIBLE
            layout_sports.visibility = View.VISIBLE
        } else if (menuItem.toInt() == 2) {
            layout_sports.visibility = View.INVISIBLE
            layout_scores.visibility = View.INVISIBLE
            tv_detail_no_result.visibility = View.VISIBLE
        } else if (menuItem.toInt() == 3) {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorites, menu)
        menuFavorites = menu
        setFavorite()
        return super.onCreateOptionsMenu(menu)
    }

    private fun setFavorite() {
        if (isFavorite) {
            menuFavorites?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorites_fill)
        } else {

            menuFavorites?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorites_border)
        }
    }

    private fun implementPutExtra() {
        idEvent = data.idEvent.toString()
        txt_date.text = getLongDate(data.dateEvent)
//        home
        idHome = data.idHomeTeam
        tv_home_score_detail.text = data.intHomeScore
        tv_name_home.text = data.strHomeTeam
        if (data.strHomeGoalDetails.equals("null")) {
            tv_goal_home_score.text = ""
        } else {
            tv_goal_home_score.text = data.strHomeGoalDetails
        }
        if (data.intHomeShots.equals("null")) {
            tv_shot_home_score.text = ""
        } else {
            tv_shot_home_score.text = data.intHomeShots
        }
        if (data.strHomeLineupGoalkeeper.equals("null")) {
            tv_gk_home.text = ""
        } else {
            tv_gk_home.text = data.strHomeLineupGoalkeeper
        }
        if (data.strHomeLineupDefense.equals("null")) {
            tv_def_home.text = ""
        } else {
            tv_def_home.text = data.strHomeLineupDefense
        }
        if (data.strHomeLineupMidfield.equals("null")) {
            tv_mid_home.text = ""
        } else {
            tv_mid_home.text = data.strHomeLineupMidfield
        }
        if (data.strHomeLineupForward.equals("null")) {
            tv_fw_home.text = ""
        } else {
            tv_fw_home.text = data.strHomeLineupForward
        }
        if (data.strHomeLineupSubstitutes.equals("null")) {
            tv_sub_home.text = ""
        } else {
            tv_sub_home.text = data.strHomeLineupSubstitutes
        }

        idAway = data.idAwayTeam
        tv_away_score_detail.text = data.intAwayScore
        tv_name_away.text = data.strAwayTeam
        if (data.strAwayGoalDetails.equals("null")) {
            tv_goal_away_score.text = ""
        } else {
            tv_goal_away_score.text = data.strAwayGoalDetails
        }
        if (data.intAwayShots.equals("null")) {
            tv_shot_away_score.text = ""
        } else {
            tv_shot_away_score.text = data.intAwayShots
        }
        if (data.strAwayLineupGoalkeeper.equals("null")) {
            tv_gk_away.text = ""
        } else {
            tv_gk_away.text = data.strAwayLineupGoalkeeper
        }
        if (data.strAwayLineupDefense.equals("null")) {
            tv_def_away.text = ""
        } else {
            tv_def_away.text = data.strAwayLineupDefense
        }
        if (data.strAwayLineupMidfield.equals("null")) {
            tv_mid_away.text = ""
        } else {
            tv_mid_away.text = data.strAwayLineupMidfield
        }
        if (data.strAwayLineupForward.equals("null")) {
            tv_fw_away.text = ""
        } else {
            tv_fw_away.text = data.strAwayLineupForward
        }
        if (data.strAwayLineupSubstitutes.equals("null")) {
            tv_sub_away.text = ""
        } else {
            tv_sub_away.text = data.strAwayLineupSubstitutes
        }
    }

    private fun setDetail() {
        isFavorite = presenter.isFavorite(ctx, data)
        idTeam = arrayOf(idHome.toString(), idAway.toString())
        if (presenter.isNetworkAvailable(this)) {
            for (i in 0 until idTeam.size) {
                //team detail
                presenter.geDetailTeam(this, idTeam[i], object : ServersCallback {
                    override fun onSuccess(response: String) {
                        try {
                            if (presenter.isSuccess(response)) {
                                if (i == 0) {
                                    val jsonObject = JSONObject(response)
                                    Log.d("TAG", "Response $jsonObject")
                                    val message = jsonObject.getJSONArray("teams")
                                    for (i in 0 until message.length()) {
                                        val data = message.getJSONObject(i)
                                        badgeHome = data.getString("strTeamBadge")
                                    }
                                    Picasso.get()
                                        .load(badgeHome)
                                        .placeholder(R.drawable.ic_baseline_browser_not_supported_24)
                                        .error(R.drawable.ic_baseline_browser_not_supported_24)
                                        .resize(175, 175)
                                        .into(tv_flag_home)
                                } else if (i == 1) {
                                    val jsonObject = JSONObject(response)
                                    Log.d("TAG", "Response $jsonObject")
                                    val message = jsonObject.getJSONArray("teams")
                                    for (i in 0 until message.length()) {
                                        val data = message.getJSONObject(i)
                                        badgeAway = data.getString("strTeamBadge")
                                    }
                                    Picasso.get()
                                        .load(badgeAway)
                                        .placeholder(R.drawable.ic_baseline_browser_not_supported_24)
                                        .error(R.drawable.ic_baseline_browser_not_supported_24)
                                        .resize(175, 175)
                                        .into(FlAG_AWAY)
                                }
                            }
                        } catch (e: NullPointerException) {

                        }
                    }

                    override fun onFailed(response: String) {
                    }

                    override fun onFailure(throwable: Throwable) {
                    }
                })
            }
        } else {
            Snackbar.make(activity_detail, getString(R.string.turnOninternet)
                , Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else if (item.itemId == mn_favorites) {
            val intent = Intent(this, LauncherActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.lightColor = Color.BLUE
                notificationManager.createNotificationChannel(notificationChannel)
                builder = Notification.Builder(this, channelId).setContentTitle("Match yang lama ~" +
                        "~").setContentText("Berhasil ditambahkan ke menu favorite").setSmallIcon(R.drawable.ic_trophy).setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable
                    .ic_launcher_background)).setContentIntent(pendingIntent)
            }
            notificationManager.notify(12345, builder.build())

            if (isFavorite) {
                presenter.removeFavorites(ctx, data)
                Log.d("TAG REMOVE", "Remove fav")
            } else {
                presenter.addFavorites(ctx, data)
                Log.d("TAG ADD", "Add fav")
            }
            isFavorite = !isFavorite
            setFavorite()
            return true
        }
        return false
    }

    override fun onBackPressed() {
        finish()
    }

}





