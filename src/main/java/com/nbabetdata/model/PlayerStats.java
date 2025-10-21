package com.nbabetdata.model;

public class PlayerStats {
    private int game_id;
    private int player_id;
    private double points;
    private double rebounds;
    private double assists;
    private double steals;
    private double blocks;
    private double turnovers;
    private double fg_made_2;
    private double fg_attempt_2;
    private double fg_made_3;
    private double fg_attempt_3;
    private double minutes;

    public int getGameId() { return game_id; }
    public int getPlayerId() { return player_id; }
    public double getPoints() { return points; }
    public double getRebounds() { return rebounds; }
    public double getAssists() { return assists; }
    // add remaining getters/setters
}
