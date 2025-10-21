package com.nbabetdata.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBSetup {

    /**
     * Initialize or reset the database schema
     */
    public static void createTables() {
        initializeDatabase();
    }

    public static void initializeDatabase() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Drop tables if you want a clean start each time
            stmt.executeUpdate("DROP TABLE IF EXISTS Players");
            stmt.executeUpdate("DROP TABLE IF EXISTS Teams");
            stmt.executeUpdate("DROP TABLE IF EXISTS Games");
            stmt.executeUpdate("DROP TABLE IF EXISTS PlayerStats");
            stmt.executeUpdate("DROP TABLE IF EXISTS PlayerAverages");
            stmt.executeUpdate("DROP TABLE IF EXISTS DefensiveStats");

            // Create Players table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Players (
                    id INTEGER PRIMARY KEY,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    team_id INTEGER,
                    position TEXT,
                    height_feet INTEGER,
                    height_inches INTEGER,
                    weight_pounds INTEGER,
                    FOREIGN KEY(team_id) REFERENCES Teams(id)
                );
            """);

            // Create Teams table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Teams (
                    id INTEGER PRIMARY KEY,
                    abbreviation TEXT,
                    city TEXT,
                    conference TEXT,
                    division TEXT,
                    full_name TEXT,
                    name TEXT
                );
            """);

            // Create Games table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Games (
                    id INTEGER PRIMARY KEY,
                    date TEXT,
                    season INTEGER,
                    status TEXT,
                    period INTEGER,
                    time TEXT,
                    postseason BOOLEAN,
                    home_team_score INTEGER,
                    visitor_team_score INTEGER,
                    home_q1 INTEGER,
                    home_q2 INTEGER,
                    home_q3 INTEGER,
                    home_q4 INTEGER,
                    home_ot1 INTEGER,
                    home_ot2 INTEGER,
                    home_ot3 INTEGER,
                    home_timeouts_remaining INTEGER,
                    home_in_bonus BOOLEAN,
                    visitor_q1 INTEGER,
                    visitor_q2 INTEGER,
                    visitor_q3 INTEGER,
                    visitor_q4 INTEGER,
                    visitor_ot1 INTEGER,
                    visitor_ot2 INTEGER,
                    visitor_ot3 INTEGER,
                    visitor_timeouts_remaining INTEGER,
                    visitor_in_bonus BOOLEAN,
                    home_team_id INTEGER,
                    visitor_team_id INTEGER,
                    FOREIGN KEY(home_team_id) REFERENCES Teams(id),
                    FOREIGN KEY(visitor_team_id) REFERENCES Teams(id)
                );
            """);

            // Keep PlayerStats, PlayerAverages, and DefensiveStats as-is
            stmt.executeUpdate("""
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
                    minutes REAL,
                    PRIMARY KEY(game_id, player_id),
                    FOREIGN KEY(game_id) REFERENCES Games(id),
                    FOREIGN KEY(player_id) REFERENCES Players(id)
                );
            """);

            stmt.executeUpdate("""
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
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS DefensiveStats (
                    player_id INTEGER PRIMARY KEY,
                    opponent_avg_points_allowed REAL,
                    opponent_avg_2pt_allowed REAL,
                    opponent_avg_3pt_allowed REAL,
                    opponent_avg_fg_percentage REAL,
                    FOREIGN KEY(player_id) REFERENCES Players(id)
                );
            """);

            System.out.println("Database initialized successfully at src/main/resources/db/nba_portal.db");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
