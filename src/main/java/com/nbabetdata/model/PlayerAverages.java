package com.nbabetdata.model;

public class PlayerAverages {

    private int playerId;
    private String name;
    private String team;
    private String position;
    private double heightFeet;
    private double heightInches;
    private double heightCm;
    private double weightKg;
    private double weight_pounds;
    private int age;

    // Player per-game averages
    private double avgPoints;
    private double avgRebounds;
    private double avgAssists;
    private double avgSteals;
    private double avgBlocks;
    private double avgTurnovers;
    private double avgMinutes;

    private double avg2ptMade;
    private double avg2ptAttempt;
    private double avg3ptMade;
    private double avg3ptAttempt;

    // Defensive matchup stats
    private double opponentAvgPointsAllowed;
    private double opponentAvg2ptAllowed;
    private double opponentAvg3ptAllowed;
    private double opponentAvgFgPercentage;

    // ===========================
    // Getters and Setters
    // ===========================

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public double getHeightCm() { return heightCm; }
    public void setHeightCm(double heightCm) { this.heightCm = heightCm; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getAvgPoints() { return avgPoints; }
    public void setAvgPoints(double avgPoints) { this.avgPoints = avgPoints; }

    public double getAvgRebounds() { return avgRebounds; }
    public void setAvgRebounds(double avgRebounds) { this.avgRebounds = avgRebounds; }

    public double getAvgAssists() { return avgAssists; }
    public void setAvgAssists(double avgAssists) { this.avgAssists = avgAssists; }

    public double getAvgSteals() { return avgSteals; }
    public void setAvgSteals(double avgSteals) { this.avgSteals = avgSteals; }

    public double getAvgBlocks() { return avgBlocks; }
    public void setAvgBlocks(double avgBlocks) { this.avgBlocks = avgBlocks; }

    public double getAvgTurnovers() { return avgTurnovers; }
    public void setAvgTurnovers(double avgTurnovers) { this.avgTurnovers = avgTurnovers; }

    public double getAvgMinutes() { return avgMinutes; }
    public void setAvgMinutes(double avgMinutes) { this.avgMinutes = avgMinutes; }

    public double getAvg2ptMade() { return avg2ptMade; }
    public void setAvg2ptMade(double avg2ptMade) { this.avg2ptMade = avg2ptMade; }

    public double getAvg2ptAttempt() { return avg2ptAttempt; }
    public void setAvg2ptAttempt(double avg2ptAttempt) { this.avg2ptAttempt = avg2ptAttempt; }

    public double getAvg3ptMade() { return avg3ptMade; }
    public void setAvg3ptMade(double avg3ptMade) { this.avg3ptMade = avg3ptMade; }

    public double getAvg3ptAttempt() { return avg3ptAttempt; }
    public void setAvg3ptAttempt(double avg3ptAttempt) { this.avg3ptAttempt = avg3ptAttempt; }

    public double getOpponentAvgPointsAllowed() { return opponentAvgPointsAllowed; }
    public void setOpponentAvgPointsAllowed(double opponentAvgPointsAllowed) { this.opponentAvgPointsAllowed = opponentAvgPointsAllowed; }

    public double getOpponentAvg2ptAllowed() { return opponentAvg2ptAllowed; }
    public void setOpponentAvg2ptAllowed(double opponentAvg2ptAllowed) { this.opponentAvg2ptAllowed = opponentAvg2ptAllowed; }

    public double getOpponentAvg3ptAllowed() { return opponentAvg3ptAllowed; }
    public void setOpponentAvg3ptAllowed(double opponentAvg3ptAllowed) { this.opponentAvg3ptAllowed = opponentAvg3ptAllowed; }

    public double getOpponentAvgFgPercentage() { return opponentAvgFgPercentage; }
    public void setOpponentAvgFgPercentage(double opponentAvgFgPercentage) { this.opponentAvgFgPercentage = opponentAvgFgPercentage; }

    public double getHeightFeet() { return heightFeet; }
    public void setHeightFeet(double heightFeet) { this.heightFeet = heightFeet; }

    public double getHeightInches() { return heightInches; }
    public void setHeightInches(double heightInches) { this.heightInches = heightInches; }

    public double getWeightPounds() { return weight_pounds; }
    public void setWeightPounds(double weight_pounds) { this.weight_pounds = weight_pounds;}
}
