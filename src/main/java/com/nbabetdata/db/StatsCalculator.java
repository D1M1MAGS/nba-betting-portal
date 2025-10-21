package com.nbabetdata.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class StatsCalculator {

    public static void calculatePlayerAverages() {
        String sql = """
            INSERT OR REPLACE INTO PlayerAverages(player_id, avg_points, avg_rebounds, avg_assists, avg_steals, avg_blocks, avg_2pt_made, avg_2pt_attempt, avg_3pt_made, avg_3pt_attempt, avg_minutes)
            SELECT player_id,
                   AVG(points),
                   AVG(rebounds),
                   AVG(assists),
                   AVG(steals),
                   AVG(blocks),
                   AVG(fg_made_2),
                   AVG(fg_attempt_2),
                   AVG(fg_made_3),
                   AVG(fg_attempt_3),
                   AVG(minutes_played)
            FROM PlayerStats
            GROUP BY player_id;
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Player averages calculated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
