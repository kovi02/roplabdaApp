package com.example.roplabdaapp.models;

public class Team {
    private String id;
    private String name;
    private String city;
    private String coach;

    public Team() {}

    public Team(String id, String name, String city, String coach) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.coach = coach;
        this.tournamentId = tournamentId;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getCoach() { return coach; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
    public void setCoach(String coach) { this.coach = coach; }

    private String tournamentId;

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }
}
