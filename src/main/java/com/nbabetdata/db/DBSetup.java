package com.nbabetdata.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBSetup {

    public static void createTables() {
        String createPlayers = """
            CREATE TABLE IF NOT EXISTS Players (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                team TEXT,
                position TEXT,
                height_cm REAL,
                weight_kg REAL,
                age INTEGER
            );
        """;

        String createTeams = """
            CREATE TABLE IF NOT EXISTS Teams (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                abbreviation TEXT
            );
        """;

        String createGames = """
            CREATE TABLE IF NOT EXISTS Games (
                id INTEGER PRIMARY KEY,
                date TEXT NOT NULL,
                home_team TEXT,
                away_team TEXT
            );
        """;

        String createPlayerStats = """
            CREATE TABLE IF NOT EXISTS PlayerStats (
                game_id INTEGER,
                player_id INTEGER,
                points REAL,
                rebounds REAL,
                assists REAL,
                steals REAL,
                blocks REAL,
                turnovers REAL,
                fg_made_2 REAL,
                fg_attempt_2 REAL,
                fg_made_3 REAL,
                fg_attempt_3 REAL,
                minutes_played REAL,
                PRIMARY KEY(game_id, player_id),
                FOREIGN KEY(game_id) REFERENCES Games(id),
                FOREIGN KEY(player_id) REFERENCES Players(id)
            );
        """;

        String createPlayerAverages = """
            CREATE TABLE IF NOT EXISTS PlayerAverages (
                player_id INTEGER PRIMARY KEY,
                avg_points REAL,
                avg_rebounds REAL,
                avg_assists REAL,
                avg_steals REAL,
                avg_blocks REAL,
                avg_2pt_made REAL,
                avg_2pt_attempt REAL,
                avg_3pt_made REAL,
                avg_3pt_attempt REAL,
                avg_minutes REAL,
                FOREIGN KEY(player_id) REFERENCES Players(id)
            );
        """;

        String createDefensiveStats = """
            CREATE TABLE IF NOT EXISTS DefensiveStats (
                player_id INTEGER PRIMARY KEY,
                opponent_avg_points_allowed REAL,
                opponent_avg_2pt_allowed REAL,
                opponent_avg_3pt_allowed REAL,
                opponent_avg_fg_percentage REAL,
                FOREIGN KEY(player_id) REFERENCES Players(id)
            );
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createPlayers);
            stmt.execute(createTeams);
            stmt.execute(createGames);
            stmt.execute(createPlayerStats);
            stmt.execute(createPlayerAverages);
            stmt.execute(createDefensiveStats);

            System.out.println("All tables created successfully!");

        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }
}
