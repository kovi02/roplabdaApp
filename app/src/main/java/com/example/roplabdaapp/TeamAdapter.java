package com.example.roplabdaapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roplabdaapp.models.Team;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private List<Team> teamList;
    private Context context;

    public TeamAdapter(List<Team> teamList, Context context) {
        this.teamList = teamList;
        this.context = context;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = teamList.get(position);

        holder.name.setText(team.getName());
        holder.city.setText(team.getCity());
        holder.coach.setText(team.getCoach());

        // Törlés
        holder.deleteBtn.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                .collection("teams")
                .document(team.getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    teamList.remove(position);
                    notifyItemRemoved(position);
                })
                .addOnFailureListener(e ->
                    Toast.makeText(v.getContext(), "Törlés sikertelen!", Toast.LENGTH_SHORT).show()
                );
        });

        // Szerkesztés
        holder.editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTeamActivity.class);
            intent.putExtra("teamId", team.getId());
            context.startActivity(intent);
        });

        // Bajnokság név lekérése
        FirebaseFirestore.getInstance()
            .collection("tournaments")
            .document(team.getTournamentId())
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String tournamentName = documentSnapshot.getString("name");
                    holder.tournamentName.setText("Bajnokság: " + tournamentName);
                } else {
                    holder.tournamentName.setText("Bajnokság: ismeretlen");
                }
            });
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView name, city, coach, tournamentName;
        Button deleteBtn, editBtn;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teamName);
            city = itemView.findViewById(R.id.teamCity);
            coach = itemView.findViewById(R.id.teamCoach);
            tournamentName = itemView.findViewById(R.id.tournamentName);
            deleteBtn = itemView.findViewById(R.id.btnDelete);
            editBtn = itemView.findViewById(R.id.btnEditTeam);
        }
    }
}
