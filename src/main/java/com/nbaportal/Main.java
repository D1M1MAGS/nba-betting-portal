package com.nbaportal;

import com.nbabetdata.db.DBSetup;
import com.nbabetdata.api.NBAApiFetcher;
import com.nbabetdata.db.StatsCalculator;

public class Main {
    public static void main(String[] args) {
        // 1. Create tables
        DBSetup.createTables();

        // 2. Fetch data from API
        NBAApiFetcher.fetchTeams();
        NBAApiFetcher.fetchPlayers();
        // NBAApiFetcher.fetchGames();
        // NBAApiFetcher.fetchPlayerStats();

        // 3. Calculate averages
        StatsCalculator.calculatePlayerAverages();

        System.out.println("Database and stats updated successfully!");
    }
}
