
package com.example.roplabdaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.*;

public class StandingsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Spinner tournamentSpinner;
    FirebaseFirestore db;
    List<StandingsItem> standingsList = new ArrayList<>();
    StandingsAdapter adapter;

    List<String> tournamentNames = new ArrayList<>();
    List<String> tournamentIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        recyclerView = findViewById(R.id.recyclerViewStandings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StandingsAdapter(standingsList);
        recyclerView.setAdapter(adapter);

        tournamentSpinner = findViewById(R.id.spinnerSelectTournament);
        db = FirebaseFirestore.getInstance();

        Button backBtn = findViewById(R.id.btnBackToMain);
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, appListActivity.class));
            finish();
        });

        Button loadBtn = findViewById(R.id.btnLoadStandings);
        loadBtn.setOnClickListener(v -> {
            int pos = tournamentSpinner.getSelectedItemPosition();
            if (pos >= 0 && pos < tournamentIds.size()) {
                String selectedId = tournamentIds.get(pos);
                loadMatchesForTournament(selectedId);
            }
        });

        loadTournaments();
    }

    private void loadTournaments() {
        db.collection("tournaments").get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                tournamentNames.clear();
                tournamentIds.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    tournamentNames.add(doc.getString("name"));
                    tournamentIds.add(doc.getId());
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tournamentNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tournamentSpinner.setAdapter(spinnerAdapter);
            });
    }

    private void loadMatchesForTournament(String tournamentId) {
        db.collection("matches")
            .whereEqualTo("tournamentId", tournamentId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                Map<String, StandingsItem> map = new HashMap<>();

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String teamA = doc.getString("teamA");
                    String teamB = doc.getString("teamB");
                    Long scoreA = doc.getLong("scoreA");
                    Long scoreB = doc.getLong("scoreB");

                    if (teamA == null || teamB == null || scoreA == null || scoreB == null) continue;

                    if (!map.containsKey(teamA)) map.put(teamA, new StandingsItem(teamA));
                    if (!map.containsKey(teamB)) map.put(teamB, new StandingsItem(teamB));

                    StandingsItem itemA = map.get(teamA);
                    StandingsItem itemB = map.get(teamB);

                    itemA.matches++;
                    itemB.matches++;

                    if (Objects.equals(scoreA, scoreB)) {
                        itemA.draws++;
                        itemB.draws++;
                        itemA.points += 1;
                        itemB.points += 1;
                    } else if (scoreA > scoreB) {
                        itemA.wins++;
                        itemB.losses++;
                        itemA.points += 3;
                    } else {
                        itemB.wins++;
                        itemA.losses++;
                        itemB.points += 3;
                    }
                }

                standingsList.clear();
                standingsList.addAll(map.values());
                standingsList.sort((a, b) -> Integer.compare(b.points, a.points));
                adapter.notifyDataSetChanged();
            });
    }
}
