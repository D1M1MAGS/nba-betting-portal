package com.nbabetdata.model;

public class Team {
    private int id;
    private String abbreviation;
    private String city;
    private String conference;
    private String division;
    private String full_name;
    private String name;

    public int getId() { return id; }
    public String getAbbreviation() { return abbreviation; }
    public String getFullName() { return full_name; }
    public String getCity() { return city; }
    public String getConference() { return conference; }
    public String getDivision() { return division; }
    public String getName() { return name; }
}
