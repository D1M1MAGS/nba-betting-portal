package com.nbabetdata.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nbabetdata.db.DBConnection;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class NBAApiFetcher {

    private static final Gson gson = new Gson();
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String API_KEY = "d2730fb8-5f66-43b2-b1b3-59cb2d284ffd";

    /** Safe getters for nullable JSON fields */
    private static String safeGetString(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : null;
    }

    private static Integer safeGetInt(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsInt() : null;
    }

    /** Fetch all teams and insert/update DB */
    public static void fetchTeams() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.balldontlie.io/v1/teams"))
                    .header("Authorization", "Bearer " + API_KEY)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("fetchTeams: HTTP status " + response.statusCode());

            if (response.statusCode() != 200) {
                System.err.println("fetchTeams request failed, skipping parsing.");
                return;
            }

            JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray teams = obj.getAsJsonArray("data");
            if (teams == null || teams.size() == 0) return;

            try (Connection conn = DBConnection.getConnection()) {
                String sql = """
                        INSERT OR REPLACE INTO Teams
                        (id, name, abbreviation, city, conference, division, full_name)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """;
                PreparedStatement ps = conn.prepareStatement(sql);

                for (int i = 0; i < teams.size(); i++) {
                    JsonObject t = teams.get(i).getAsJsonObject();
                    ps.setInt(1, safeGetInt(t, "id"));
                    ps.setString(2, safeGetString(t, "name"));
                    ps.setString(3, safeGetString(t, "abbreviation"));
                    ps.setString(4, safeGetString(t, "city"));
                    ps.setString(5, safeGetString(t, "conference"));
                    ps.setString(6, safeGetString(t, "division"));
                    ps.setString(7, safeGetString(t, "full_name"));
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            System.out.println("Teams fetched successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Fetch all players and insert/update DB */
    public static void fetchPlayers() {
        try {
            int page = 1;
            boolean morePages = true;

            while (morePages) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.balldontlie.io/v1/players?per_page=100&page=" + page))
                        .header("Authorization", "Bearer " + API_KEY)
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("fetchPlayers: page " + page + " HTTP status " + response.statusCode());

                if (response.statusCode() != 200) break;

                JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray players = obj.getAsJsonArray("data");
                if (players == null || players.size() == 0) break;

                try (Connection conn = DBConnection.getConnection()) {
                    String sql = """
                            INSERT OR REPLACE INTO Players
                            (id, first_name, last_name, team_id, position, height_feet, height_inches, weight_pounds)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                            """;
                    PreparedStatement ps = conn.prepareStatement(sql);

                    for (int i = 0; i < players.size(); i++) {
                        JsonObject p = players.get(i).getAsJsonObject();
                        ps.setInt(1, safeGetInt(p, "id"));
                        ps.setString(2, safeGetString(p, "first_name"));
                        ps.setString(3, safeGetString(p, "last_name"));

                        JsonObject team = p.getAsJsonObject("team");
                        ps.setInt(4, team != null && !team.get("id").isJsonNull() ? team.get("id").getAsInt() : null);

                        ps.setString(5, safeGetString(p, "position"));

                        // parse height like "6-6"
                        String heightStr = safeGetString(p, "height");
                        if (heightStr != null && heightStr.contains("-")) {
                            String[] parts = heightStr.split("-");
                            ps.setInt(6, Integer.parseInt(parts[0]));
                            ps.setInt(7, Integer.parseInt(parts[1]));
                        } else {
                            ps.setNull(6, java.sql.Types.INTEGER);
                            ps.setNull(7, java.sql.Types.INTEGER);
                        }

                        Integer weight = safeGetInt(p, "weight");
                        ps.setInt(8, weight != null ? weight : 0);

                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                page++;
                JsonObject meta = obj.getAsJsonObject("meta");
                morePages = meta != null && meta.has("next_page") && !meta.get("next_page").isJsonNull();
            }

            System.out.println("Players fetched successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Fetch games for a given season and insert/update DB */
    public static void fetchGames(int seasonYear) {
        try {
            int page = 1;
            boolean morePages = true;

            while (morePages) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.balldontlie.io/v1/games?seasons[]=" + seasonYear + "&per_page=100&page=" + page))
                        .header("Authorization", "Bearer " + API_KEY)
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("fetchGames: page " + page + " HTTP status " + response.statusCode());

                if (response.statusCode() != 200) break;

                JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray games = obj.getAsJsonArray("data");
                if (games == null || games.size() == 0) break;

                try (Connection conn = DBConnection.getConnection()) {
                    String sql = """
                            INSERT OR REPLACE INTO Games
                            (id, home_team_id, visitor_team_id, date, home_team_score, visitor_team_score)
                            VALUES (?, ?, ?, ?, ?, ?)
                            """;
                    PreparedStatement ps = conn.prepareStatement(sql);

                    for (int i = 0; i < games.size(); i++) {
                        JsonObject g = games.get(i).getAsJsonObject();
                        ps.setInt(1, safeGetInt(g, "id"));

                        JsonObject homeTeam = g.getAsJsonObject("home_team");
                        JsonObject visitorTeam = g.getAsJsonObject("visitor_team");

                        ps.setInt(2, homeTeam != null ? safeGetInt(homeTeam, "id") : 0);
                        ps.setInt(3, visitorTeam != null ? safeGetInt(visitorTeam, "id") : 0);

                        ps.setString(4, safeGetString(g, "date"));
                        ps.setInt(5, safeGetInt(g, "home_team_score") != null ? safeGetInt(g, "home_team_score") : 0);
                        ps.setInt(6, safeGetInt(g, "visitor_team_score") != null ? safeGetInt(g, "visitor_team_score") : 0);

                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                page++;
                JsonObject meta = obj.getAsJsonObject("meta");
                morePages = meta != null && meta.has("next_page") && !meta.get("next_page").isJsonNull();
            }

            System.out.println("Games fetched successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
