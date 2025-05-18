package com.example.roplabdaapp.models;

public class Tournament {
    private String id;
    private String name;
    private String startDate;

    public Tournament() {}

    public Tournament(String id, String name, String startDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getStartDate() { return startDate; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
}
