package com.example.roplabdaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.roplabdaapp.models.Team;
import com.example.roplabdaapp.models.Tournament;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddTeamActivity extends AppCompatActivity {

    EditText nameInput, cityInput, coachInput;
    Button saveBtn;
    Spinner tournamentSpinner;

    FirebaseFirestore db;
    List<Tournament> tournamentList = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;
    List<String> tournamentNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }


        nameInput = findViewById(R.id.inputTeamName);
        cityInput = findViewById(R.id.inputTeamCity);
        coachInput = findViewById(R.id.inputTeamCoach);
        saveBtn = findViewById(R.id.saveTeamBtn);
        tournamentSpinner = findViewById(R.id.spinnerTournament);
        db = FirebaseFirestore.getInstance();

        loadTournaments();

        saveBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String city = cityInput.getText().toString().trim();
            String coach = coachInput.getText().toString().trim();
            int selectedIndex = tournamentSpinner.getSelectedItemPosition();
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            saveBtn.startAnimation(anim);


            if (name.isEmpty() || city.isEmpty() || coach.isEmpty()) {
                Toast.makeText(this, "Minden mezőt tölts ki!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedIndex < 0 || selectedIndex >= tournamentList.size()) {
                Toast.makeText(this, "Válassz ki egy bajnokságot!", Toast.LENGTH_SHORT).show();
                return;
            }

            String tournamentId = tournamentList.get(selectedIndex).getId();
            String id = UUID.randomUUID().toString();
            Team newTeam = new Team(id, name, city, coach);
            newTeam.setTournamentId(tournamentId);

            db.collection("teams").document(id).set(newTeam)
                .addOnSuccessListener(unused -> {
                    showNotification("A csapat sikeresen mentve!");
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Hiba történt.", Toast.LENGTH_SHORT).show());
        });

        Button backBtn = findViewById(R.id.btnBackToMain);
        backBtn.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            backBtn.startAnimation(anim);

            Intent intent = new Intent(this, appListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void showNotification(String message) {
        String channelId = "team_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Team Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Röplabda App")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            //
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(1001, builder.build());
    }


    private void loadTournaments() {
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
            });
    }
}
