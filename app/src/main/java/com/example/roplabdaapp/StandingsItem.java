package com.example.roplabdaapp;

public class StandingsItem {
    public String teamName;
    public int matches;
    public int wins;
    public int losses;
    public int draws;
    public int points;

    public StandingsItem(String teamName) {
        this.teamName = teamName;
        this.matches = 0;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.points = 0;
    }
}
