package com.example.uas_0127.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.uas_0127.EventItem
import org.jetbrains.anko.db.*

class FavoritesDbOpenHelper(context: Context) : ManagedSQLiteOpenHelper(context, "Favorites.db", null, 1) {

    companion object {
        private var instance: FavoritesDbOpenHelper? = null
        @Synchronized
        fun getInstance(context: Context): FavoritesDbOpenHelper {
            if (instance == null) {
                instance = FavoritesDbOpenHelper(context.applicationContext)
            }
            return instance as FavoritesDbOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            EventItem.TABLE_FAVORITES, true,
            EventItem.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            EventItem.ID_EVENT to TEXT,
            EventItem.DATE to TEXT,
            EventItem.STR_SPORT to TEXT,

            // home team
            EventItem.HOME_ID to TEXT,
            EventItem.HOME_TEAM to TEXT,
            EventItem.HOME_SCORE to TEXT,
            EventItem.HOME_FORMATION to TEXT,
            EventItem.HOME_GOAL_DETAILS to TEXT,
            EventItem.HOME_SHOTS to TEXT,
            EventItem.HOME_LINEUP_GOALKEEPER to TEXT,
            EventItem.HOME_LINEUP_DEFENSE to TEXT,
            EventItem.HOME_LINEUP_MIDFIELD to TEXT,
            EventItem.HOME_LINEUP_FORWARD to TEXT,
            EventItem.HOME_LINEUP_SUBSTITUTES to TEXT,

            // away team
            EventItem.AWAY_ID to TEXT,
            EventItem.AWAY_TEAM to TEXT,
            EventItem.AWAY_SCORE to TEXT,
            EventItem.AWAY_FORMATION to TEXT,
            EventItem.AWAY_GOAL_DETAILS to TEXT,
            EventItem.AWAY_SHOTS to TEXT,
            EventItem.AWAY_LINEUP_GOALKEEPER to TEXT,
            EventItem.AWAY_LINEUP_DEFENSE to TEXT,
            EventItem.AWAY_LINEUP_MIDFIELD to TEXT,
            EventItem.AWAY_LINEUP_FORWARD to TEXT,
            EventItem.AWAY_LINEUP_SUBSTITUTES to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(EventItem.TABLE_FAVORITES, true)
    }
}

val Context.database: FavoritesDbOpenHelper
    get() = FavoritesDbOpenHelper.getInstance(applicationContext)
