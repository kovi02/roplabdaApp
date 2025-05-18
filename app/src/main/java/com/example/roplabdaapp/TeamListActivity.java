package com.example.roplabdaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.example.roplabdaapp.models.Team;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class TeamListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TeamAdapter adapter;
    List<Team> teamList = new ArrayList<>();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        recyclerView = findViewById(R.id.recyclerViewTeams);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TeamAdapter(teamList, this);
        recyclerView.setAdapter(adapter);

        Button backBtn = findViewById(R.id.btnBackToMain);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, appListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });


        db = FirebaseFirestore.getInstance();
        loadTeams();
    }

    private void loadTeams() {
        db.collection("teams")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    teamList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Team team = doc.toObject(Team.class);
                        teamList.add(team);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Hiba a csapatok betöltésekor.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTeams(); // újra lekérjük a frissített adatokat
    }

}
