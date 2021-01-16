package com.example.uas_0127

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.example.uas_0127.R
import com.example.uas_0127.detail.INTENT_DETAIL
import com.example.uas_0127.EventItem
import com.example.uas_0127.LeaguesItem
import com.example.uas_0127.detail.DetailActivity
import com.example.uas_0127.utils.ServersCallback
import com.example.uas_0127.utils.gone
import com.example.uas_0127.utils.invisible
import com.example.uas_0127.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.ctx
import org.json.JSONObject

class MainActivity : AppCompatActivity(), SportsView {

    private var listLeageu = ArrayList<LeaguesItem>()
    private var menuItem: Int = 1
    private var idSpinner: String = ""
    private val presenter = SportsPresenters(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        getlistLeageu()
        setSpinner(menuItem)
        initBottomNavigationContainer()
        initContainer()

        btn_refresh.setOnClickListener {
            if (presenter.isNetworkAvailable(this)) {
                this@MainActivity.finish()
                this@MainActivity.startActivity(this@MainActivity.intent)
            } else {
                Snackbar.make(main_activity, getString(R.string.turnOninternet)
                    , Snackbar.LENGTH_LONG).show()
            }
        }

        swipe_refresh.setOnRefreshListener {
            if (swipe_refresh.isRefreshing) {
                swipe_refresh.isRefreshing = false
                setSpinner(menuItem)
                setDataToContainer(idSpinner, menuItem)
            }

        }
    }

    private fun initLayout() {
        setContentView(R.layout.activity_main)
    }

    private fun initBottomNavigationContainer() {
        navigation.setOnNavigationItemSelectedListener(bottomNavigationListener)
    }

    private val bottomNavigationListener by lazy {
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_menu_prev_match -> {
                    spinner.visibility = View.VISIBLE
                    menuItem = 1
                    title = getString(R.string.app_name)
                    setSpinner(menuItem)
                    setDataToContainer(idSpinner, menuItem)
                    Log.d("TAG", "ini prev")
                    true
                }
                R.id.main_menu_next_match -> {
                    spinner.visibility = View.VISIBLE
                    menuItem = 2
                    title = getString(R.string.app_name)
                    setSpinner(menuItem)
                    setDataToContainer(idSpinner, menuItem)
                    Log.d("TAG", "ini next")
                    true
                }
                R.id.main_menu_favorites_match -> {
                    menuItem = 3
                    title = getString(R.string.app_name)
                    setSpinner(menuItem)
                    spinner.visibility = View.GONE
                    setDataToContainer("0", menuItem)
                    Log.d("TAG", "ini fav")
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    override fun showLoading() {
        progressbar.visible()
        rv.invisible()
        empty_DataView.invisible()
        no_conectionView.invisible()
    }

    override fun hideLoading() {
        progressbar.gone()
        rv.visible()
        empty_DataView.invisible()
        no_conectionView.invisible()
    }

    override fun showEmptyData() {
        progressbar.gone()
        rv.invisible()
        if (presenter.isNetworkAvailable(this)) {
            empty_DataView.visible()
        } else {
            no_conectionView.visible()

        }
    }

    private fun getlistLeageu() {
        if (presenter.isNetworkAvailable(this)) {
            presenter.getSpinnerData(this, object : ServersCallback {
                override fun onSuccess(response: String) {
                    if (presenter.isSuccess(response)) {
                        try {
                            val jsonObject = JSONObject(response)
                            var numData = 0
                            var numDataSize = 0
                            val message = jsonObject.getJSONArray("leagues")
                            val soccerData = arrayOfNulls<String>(message.length())
                            val idLeague: ArrayList<String> = ArrayList()
                            val leagueName: ArrayList<String> = ArrayList()
                            Log.d("TAG MESSAGE", message.length().toString())
                            if (message.length() > 0) {
                                for (i in 0 until message.length()) {
                                    val dataAll = message.getJSONObject(i)
                                    soccerData[i] = dataAll.getString("strSport")
                                    if (soccerData[i].toString() == "Soccer") {
                                        idLeague.add(dataAll.getString("idLeague"))
                                        leagueName.add(dataAll.getString("strLeague"))
                                        listLeageu.add(LeaguesItem(idLeague[numData], leagueName[numData]))
                                        numData += 1
                                    } else {
                                        numDataSize += 1
                                    }
                                }
                                Log.d("TAG NUMDATA", numData.toString())
                                Log.d("TAG NUMDATASIZE", numDataSize.toString())
                                spinner.adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, leagueName)
                            } else {
                            }
                        } catch (e: NullPointerException) {
                            showEmptyData()
                        }
                    }
                }

                override fun onFailed(response: String) {
                }

                override fun onFailure(throwable: Throwable) {
                }
            })
        } else {
            showEmptyData()
            Snackbar.make(main_activity, getString(R.string.turnOninternet)
                , Snackbar.LENGTH_LONG).show()
        }

    }

    private fun setSpinner(menu: Int) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                idSpinner = listLeageu[position].idLeague
                when (menu) {
                    1 -> setDataToContainer(idSpinner, 1)
                    2 -> setDataToContainer(idSpinner, 2)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun initContainer() {
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = SportsAdapter { posistionData ->
            val dataIntent = getListAdapter()?.getDataAt(posistionData)
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(INTENT_DETAIL, dataIntent)
            intent.putExtra(getString(R.string.menu_item), menuItem.toString())
            startActivity(intent)
        }
    }

    private fun setDataToContainer(id: String, menu: Int) {
        var data: MutableList<EventItem>
        if (presenter.isNetworkAvailable(this)) {
            if (menu == 1) {
                showLoading()
                presenter.getPrevMatch(this, id, object : ServersCallback {
                    override fun onSuccess(response: String) {
                        if (presenter.isSuccess(response)) {
                            try {
                                if (presenter.isSuccess(response)) {
                                    data = presenter.parsingData(this@MainActivity, response)
                                    if (data.size < 1) {
                                        showEmptyData()
                                    } else {
                                        getListAdapter()?.setData(data.toMutableList())
                                        hideLoading()
                                    }
                                }
                            } catch (e: NullPointerException) {
                                showEmptyData()
                            }
                        }
                    }

                    override fun onFailed(response: String) {
                        showEmptyData()
                    }

                    override fun onFailure(throwable: Throwable) {
                        showEmptyData()
                    }
                })
            } else if (menu == 2) {
                showLoading()
                presenter.getNextMatch(this, id, object : ServersCallback {
                    override fun onSuccess(response: String) {
                        if (presenter.isSuccess(response)) {
                            try {
                                if (presenter.isSuccess(response)) {
                                    data = presenter.parsingData(this@MainActivity, response)
                                    if (data.size < 1) {
                                        showEmptyData()
                                    } else {
                                        getListAdapter()?.setData(data.toMutableList())
                                        hideLoading()
                                    }
                                }
                            } catch (e: NullPointerException) {
                                showEmptyData()
                            }
                        }
                    }

                    override fun onFailed(response: String) {
                        showEmptyData()
                    }

                    override fun onFailure(throwable: Throwable) {
                        showEmptyData()
                    }
                })
            } else if (menu == 3) {
                var data: MutableList<EventItem>
                data = presenter.getFavoritesAll(ctx)
                if (data.size < 1) {
                    showEmptyData()
                } else {
                    getListAdapter()?.setData(data.toMutableList())
                }

            } else {
                showEmptyData()
            }
        } else {
            showEmptyData()
            Snackbar.make(main_activity, getString(R.string.turnOninternet)
                , Snackbar.LENGTH_LONG).show()
        }
    }

    private fun getListAdapter(): SportsAdapter? = rv?.adapter as? SportsAdapter
}

