package com.nbabetdata;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nbabetdata.db.DBSetup;
import com.nbabetdata.api.NBAApiFetcher;
import com.nbabetdata.db.StatsCalculator;

@SpringBootApplication
public class NbaPortalApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NbaPortalApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing database and fetching NBA data...");

        // 1. Create tables if not exist (drops old data)
        DBSetup.createTables();

        // 2. Fetch live data from API
        NBAApiFetcher.fetchTeams();
        NBAApiFetcher.fetchPlayers();
        NBAApiFetcher.fetchGames(2025);
        //NBAApiFetcher.fetchPlayerStats(); TODO: Implemeent this when paid version is available

        // 3. Calculate player averages & defensive stats
        StatsCalculator.calculatePlayerAverages();

        System.out.println("Database populated and stats calculated!");
    }
}
