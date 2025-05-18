package com.example.roplabdaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMatchActivity extends AppCompatActivity {

    Spinner spinnerTournament, spinnerTeamA, spinnerTeamB;
    EditText scoreAInput, scoreBInput;
    Button btnSaveMatch;

    FirebaseFirestore db;
    List<String> tournamentIds = new ArrayList<>();
    List<String> tournamentNames = new ArrayList<>();
    List<String> teamNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);

        spinnerTournament = findViewById(R.id.spinnerTournament);
        spinnerTeamA = findViewById(R.id.spinnerTeamA);
        spinnerTeamB = findViewById(R.id.spinnerTeamB);
        scoreAInput = findViewById(R.id.editScoreA);
        scoreBInput = findViewById(R.id.editScoreB);
        btnSaveMatch = findViewById(R.id.btnSaveMatch);

        db = FirebaseFirestore.getInstance();

        loadTournaments();

        btnSaveMatch.setOnClickListener(v -> {
            int tourIndex = spinnerTournament.getSelectedItemPosition();
            String teamA = spinnerTeamA.getSelectedItem().toString();
            String teamB = spinnerTeamB.getSelectedItem().toString();

            Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            btnSaveMatch.startAnimation(anim);

            if (teamA.equals(teamB)) {
                Toast.makeText(this, "A két csapat nem lehet ugyanaz!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int scoreA = Integer.parseInt(scoreAInput.getText().toString().trim());
                int scoreB = Integer.parseInt(scoreBInput.getText().toString().trim());

                Map<String, Object> match = new HashMap<>();
                match.put("tournamentId", tournamentIds.get(tourIndex));
                match.put("teamA", teamA);
                match.put("teamB", teamB);
                match.put("scoreA", scoreA);
                match.put("scoreB", scoreB);

                db.collection("matches").add(match)
                        .addOnSuccessListener(doc -> {
                            Toast.makeText(this, "Meccs elmentve!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Hiba történt!", Toast.LENGTH_SHORT).show();
                        });

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Érvénytelen eredmény!", Toast.LENGTH_SHORT).show();
            }
        });

        Button backBtn = findViewById(R.id.btnBackToMain);
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, appListActivity.class));
            finish();
        });

    }

    private void loadTournaments() {
        db.collection("tournaments").get()
            .addOnSuccessListener(snapshot -> {
                tournamentIds.clear();
                tournamentNames.clear();
                for (QueryDocumentSnapshot doc : snapshot) {
                    tournamentIds.add(doc.getId());
                    tournamentNames.add(doc.getString("name"));
                }
                tournamentNames.add(0, "Válassz bajnokságot...");
                tournamentIds.add(0, "");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tournamentNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTournament.setAdapter(adapter);

                loadTeams();
            });
    }

    private void loadTeams() {
        db.collection("teams").get()
            .addOnSuccessListener(snapshot -> {
                teamNames.clear();
                for (QueryDocumentSnapshot doc : snapshot) {
                    teamNames.add(doc.getString("name"));
                }
                ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNames);
                teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTeamA.setAdapter(teamAdapter);
                spinnerTeamB.setAdapter(teamAdapter);
            });
    }
}
