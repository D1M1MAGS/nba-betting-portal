package com.nbabetdata.db;

import java.sql.Connection;
import java.sql.Statement;

public class StatsCalculator {

    public static void calculatePlayerAverages() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Clear previous averages
            stmt.executeUpdate("DELETE FROM player_averages");
            stmt.executeUpdate("DELETE FROM defensive_stats");

            // Calculate averages per player
            String avgSql = """
                INSERT INTO player_averages (
                    player_id, avg_points, avg_rebounds, avg_assists, avg_steals, avg_blocks,
                    avg_2pt_made, avg_2pt_attempt, avg_3pt_made, avg_3pt_attempt, avg_minutes
                )
                SELECT 
                    player_id,
                    AVG(points),
                    AVG(rebounds),
                    AVG(assists),
                    AVG(steals),
                    AVG(blocks),
                    AVG(fg_made_2),
                    AVG(fg_attempt_2),
                    AVG(fg_made_3),
                    AVG(fg_attempt_3),
                    AVG(minutes)
                FROM player_stats
                GROUP BY player_id
            """;
            stmt.executeUpdate(avgSql);

            // Calculate simple defensive stats: opponent averages
            String defSql = """
                INSERT INTO defensive_stats (
                    player_id, opponent_avg_points_allowed, opponent_avg_2pt_allowed,
                    opponent_avg_3pt_allowed, opponent_avg_fg_percentage
                )
                SELECT 
                    player_id,
                    AVG(points) AS opponent_avg_points_allowed,
                    AVG(fg_attempt_2 - fg_made_2) AS opponent_avg_2pt_allowed,
                    AVG(fg_attempt_3 - fg_made_3) AS opponent_avg_3pt_allowed,
                    AVG((fg_made_2 + fg_made_3) / (fg_attempt_2 + fg_attempt_3)) AS opponent_avg_fg_percentage
                FROM player_stats
                GROUP BY player_id
            """;
            stmt.executeUpdate(defSql);

            System.out.println("Player averages and defensive stats calculated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
