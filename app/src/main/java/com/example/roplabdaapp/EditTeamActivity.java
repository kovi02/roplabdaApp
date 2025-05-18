package com.example.roplabdaapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.example.roplabdaapp.models.Team;
import com.example.roplabdaapp.models.Tournament;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EditTeamActivity extends AppCompatActivity {

    EditText nameInput, cityInput, coachInput;
    Spinner tournamentSpinner;
    Button saveChangesBtn;
    FirebaseFirestore db;
    String teamId;

    List<Tournament> tournamentList = new ArrayList<>();
    List<String> tournamentNames = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;

    String selectedTournamentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        nameInput = findViewById(R.id.editTeamName);
        cityInput = findViewById(R.id.editTeamCity);
        coachInput = findViewById(R.id.editTeamCoach);
        tournamentSpinner = findViewById(R.id.spinnerEditTournament);
        saveChangesBtn = findViewById(R.id.btnSaveTeamChanges);
        db = FirebaseFirestore.getInstance();

        teamId = getIntent().getStringExtra("teamId");

        loadTournaments(() -> {
            if (teamId != null) {
                DocumentReference teamRef = db.collection("teams").document(teamId);
                teamRef.get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        Team team = snapshot.toObject(Team.class);
                        if (team != null) {
                            nameInput.setText(team.getName());
                            cityInput.setText(team.getCity());
                            coachInput.setText(team.getCoach());

                            String tournamentId = team.getTournamentId();
                            if (tournamentId != null){
                            // beállítjuk a spinner aktuális értékét
                            for (int i = 0; i < tournamentList.size(); i++) {
                                if (tournamentList.get(i).getId().equals(team.getTournamentId())) {
                                    tournamentSpinner.setSelection(i);
                                    break;
                                }
                            }
                            }
                        }
                    }
                });
            }
        });

        saveChangesBtn.setOnClickListener(v -> {
            String newName = nameInput.getText().toString().trim();
            String newCity = cityInput.getText().toString().trim();
            String newCoach = coachInput.getText().toString().trim();
            int selectedIndex = tournamentSpinner.getSelectedItemPosition();

            Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            saveChangesBtn.startAnimation(anim);


            if (newName.isEmpty() || newCity.isEmpty() || newCoach.isEmpty()) {
                Toast.makeText(this, "Minden mezőt ki kell tölteni!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedIndex < 0 || selectedIndex >= tournamentList.size()) {
                Toast.makeText(this, "Válassz ki egy bajnokságot!", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedTournamentId = tournamentList.get(selectedIndex).getId();

            db.collection("teams").document(teamId)
                .update("name", newName, "city", newCity, "coach", newCoach, "tournamentId", selectedTournamentId)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Módosítás sikeres!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba történt a mentéskor!", Toast.LENGTH_SHORT).show();
                });
        });
    }

    private void loadTournaments(Runnable onLoaded) {
        db.collection("tournaments").get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                tournamentList.clear();
                tournamentNames.clear();

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Tournament t = doc.toObject(Tournament.class);
                    tournamentList.add(t);
                    tournamentNames.add(t.getName());
                }

                spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tournamentNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tournamentSpinner.setAdapter(spinnerAdapter);

                onLoaded.run();
            });
    }
}
