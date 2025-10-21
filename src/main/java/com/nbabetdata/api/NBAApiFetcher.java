package com.nbabetdata.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nbabetdata.model.Player;
import com.nbabetdata.model.Team;
import com.nbabetdata.db.DBConnection;

import java.net.http.*;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NBAApiFetcher {

    private static final Gson gson = new Gson();
    private static final HttpClient client = HttpClient.newHttpClient();

    /** Fetch all players from balldontlie and insert/update DB */
    public static void fetchPlayers() {
        try {
            int page = 1;
            boolean morePages = true;

            while (morePages) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://www.balldontlie.io/api/v1/players?per_page=100&page=" + page))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject obj = gson.fromJson(response.body(), JsonObject.class);
                JsonArray players = obj.getAsJsonArray("data");

                if (players.size() == 0) break;

                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "INSERT OR REPLACE INTO Players (id, name, team, position) VALUES (?,?,?,?)";
                    PreparedStatement ps = conn.prepareStatement(sql);

                    for (int i = 0; i < players.size(); i++) {
                        JsonObject p = players.get(i).getAsJsonObject();
                        ps.setInt(1, p.get("id").getAsInt());
                        ps.setString(2, p.get("first_name").getAsString() + " " + p.get("last_name").getAsString());
                        JsonObject team = p.getAsJsonObject("team");
                        ps.setString(3, team.get("full_name").getAsString());
                        ps.setString(4, p.get("position").getAsString());
                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                page++;
                morePages = !obj.get("meta").getAsJsonObject().get("next_page").isJsonNull();
            }

            System.out.println("Players fetched successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Fetch all teams */
    public static void fetchTeams() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.balldontlie.io/api/v1/teams"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray teams = gson.fromJson(response.body(), JsonArray.class);

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT OR REPLACE INTO Teams (id, name, abbreviation) VALUES (?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql);

                for (int i = 0; i < teams.size(); i++) {
                    JsonObject t = teams.get(i).getAsJsonObject();
                    ps.setInt(1, t.get("id").getAsInt());
                    ps.setString(2, t.get("full_name").getAsString());
                    ps.setString(3, t.get("abbreviation").getAsString());
                    ps.addBatch();
                }

                ps.executeBatch();
            }

            System.out.println("Teams fetched successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
