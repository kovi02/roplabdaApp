package com.example.roplabdaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roplabdaapp.models.Tournament;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import android.widget.Button;
import android.widget.Toast;

public class TournamentAdapter extends RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder> {

    private List<Tournament> tournamentList;

    public TournamentAdapter(List<Tournament> tournamentList) {
        this.tournamentList = tournamentList;
    }

    @NonNull
    @Override
    public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tournament, parent, false);
        return new TournamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentViewHolder holder, int position) {
        Tournament t = tournamentList.get(position);
        holder.name.setText(t.getName());
        
        holder.deleteBtn.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                .collection("tournaments")
                .document(t.getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    tournamentList.remove(position);
                    notifyItemRemoved(position);
                })
                .addOnFailureListener(e ->
                    Toast.makeText(v.getContext(), "Törlés sikertelen!", Toast.LENGTH_SHORT).show()
                );
        });
    
holder.date.setText("Kezdés: " + t.getStartDate());
    }

    @Override
    public int getItemCount() {
        return tournamentList.size();
    }

    public static class TournamentViewHolder extends RecyclerView.ViewHolder {
        TextView name, date;
        Button deleteBtn;

        public TournamentViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteBtn = itemView.findViewById(R.id.btnDeleteTournament);
            name = itemView.findViewById(R.id.tournamentName);
            date = itemView.findViewById(R.id.tournamentDate);
        }
    }
}
