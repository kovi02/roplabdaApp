package com.example.roplabdaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.roplabdaapp.models.Tournament;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class TournamentActivity extends AppCompatActivity {

    EditText inputName, inputDate;
    Button addTournamentBtn;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<Tournament> tournamentList;
    TournamentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        inputName = findViewById(R.id.inputTournamentName);
        inputDate = findViewById(R.id.inputTournamentDate);
        addTournamentBtn = findViewById(R.id.btnAddTournament);
        recyclerView = findViewById(R.id.recyclerViewTournaments);
        db = FirebaseFirestore.getInstance();

        tournamentList = new ArrayList<>();
        adapter = new TournamentAdapter(tournamentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        addTournamentBtn.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String date = inputDate.getText().toString().trim();
            if (name.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Minden mezőt ki kell tölteni!", Toast.LENGTH_SHORT).show();
                return;
            }
            String id = UUID.randomUUID().toString();
            Tournament t = new Tournament(id, name, date);
            db.collection("tournaments").document(id).set(t)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Bajnokság hozzáadva", Toast.LENGTH_SHORT).show();
                    tournamentList.add(t);
                    adapter.notifyItemInserted(tournamentList.size() - 1);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba történt!", Toast.LENGTH_SHORT).show();
                });
        });

        Button backBtn = findViewById(R.id.btnBackToMain);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, appListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });


        loadTournaments();
    }

    private void loadTournaments() {
        db.collection("tournaments").get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                tournamentList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Tournament t = doc.toObject(Tournament.class);
                    tournamentList.add(t);
                }
                adapter.notifyDataSetChanged();
            });
    }
}
