package com.nbabetdata.controller;

import com.nbabetdata.db.DBConnection;
import com.nbabetdata.model.PlayerAverages;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PlayerController {

    @GetMapping("/players")
    public List<PlayerAverages> getPlayers() {
        List<PlayerAverages> players = new ArrayList<>();

        String query = "SELECT * FROM PlayerAverages pa " +
                       "JOIN Players p ON pa.player_id = p.id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                PlayerAverages player = new PlayerAverages();
                player.setPlayerId(rs.getInt("id"));
                player.setName(rs.getString("name"));
                player.setTeam(rs.getString("team"));
                player.setPosition(rs.getString("position"));
                player.setHeightCm(rs.getDouble("height"));
                player.setWeightKg(rs.getDouble("weight"));
                player.setAge(rs.getInt("age"));

                // Per-game stats
                player.setAvgPoints(rs.getDouble("avg_points"));
                player.setAvgRebounds(rs.getDouble("avg_rebounds"));
                player.setAvgAssists(rs.getDouble("avg_assists"));
                player.setAvgSteals(rs.getDouble("avg_steals"));
                player.setAvgBlocks(rs.getDouble("avg_blocks"));
                player.setAvgTurnovers(rs.getDouble("avg_turnovers"));
                player.setAvgMinutes(rs.getDouble("avg_minutes"));
                player.setAvg2ptMade(rs.getDouble("avg_2pt_made"));
                player.setAvg2ptAttempt(rs.getDouble("avg_2pt_attempt"));
                player.setAvg3ptMade(rs.getDouble("avg_3pt_made"));
                player.setAvg3ptAttempt(rs.getDouble("avg_3pt_attempt"));

                // Defensive stats
                player.setOpponentAvgPointsAllowed(rs.getDouble("opponent_avg_points_allowed"));
                player.setOpponentAvg2ptAllowed(rs.getDouble("opponent_avg_2pt_allowed"));
                player.setOpponentAvg3ptAllowed(rs.getDouble("opponent_avg_3pt_allowed"));
                player.setOpponentAvgFgPercentage(rs.getDouble("opponent_avg_fg_percentage"));

                players.add(player);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return players;
    }
}
