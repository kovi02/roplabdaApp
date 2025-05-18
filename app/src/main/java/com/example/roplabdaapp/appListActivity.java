package com.example.roplabdaapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class appListActivity extends AppCompatActivity {

    Button btnAddTeam, btnViewTeams, btnTournaments, btnStandings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        btnAddTeam = findViewById(R.id.btnAddTeam);
        btnViewTeams = findViewById(R.id.btnViewTeams);
        btnTournaments = findViewById(R.id.btnTournaments);
        btnStandings = findViewById(R.id.btnStandings);

        Button btnAddMatch = findViewById(R.id.btnAddMatch);
        btnAddMatch.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddMatchActivity.class);
            startActivity(intent);
        });

        btnAddTeam.setOnClickListener(v -> startActivity(new Intent(this, AddTeamActivity.class)));
        btnViewTeams.setOnClickListener(v -> startActivity(new Intent(this, TeamListActivity.class)));
        btnTournaments.setOnClickListener(v -> startActivity(new Intent(this, TournamentActivity.class)));
        btnStandings.setOnClickListener(v -> startActivity(new Intent(this, StandingsActivity.class)));
    }
}
