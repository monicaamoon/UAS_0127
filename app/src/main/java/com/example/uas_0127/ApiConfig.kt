package com.example.uas_0127

class ApiConfig {
    companion object {
        // base end point
        const val END_POINT = "https://www.thesportsdb.com/api/v1/json/1/"
        // all leagues ( spinner )
        const val allLeagues = "all_leagues.php"
        // prev match
        const val eventsPastleague = "eventspastleague.php"
        //next match
        const val eventsNextleague = "eventsnextleague.php"
        // detail team
        const val lookupTeam = "lookupteam.php"
    }
}