package com.nbabetdata.model;

public class Player {
    private int id;
    private String first_name;
    private String last_name;
    private String position;
    private int teamId;
    private int age;       // You can calculate from birthdate if API provides it

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return first_name + " " + last_name; }
    public String getPosition() { return position; }
    public int getTeamId() { return teamId; }

    public void setTeamId(int teamId) { this.teamId = teamId; }
    public void setAge(int age) { this.age = age; }
    public int getAge() { return age; }
}
