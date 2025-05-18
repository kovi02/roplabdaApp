package com.example.roplabdaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StandingsAdapter extends RecyclerView.Adapter<StandingsAdapter.ViewHolder> {

    private List<StandingsItem> standingsList;

    public StandingsAdapter(List<StandingsItem> standingsList) {
        this.standingsList = standingsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_standings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StandingsItem item = standingsList.get(position);
        holder.teamName.setText(item.teamName);
        holder.matches.setText("Meccsek: " + item.matches);
        holder.results.setText("Gy: " + item.wins + " D: " + item.draws + " V: " + item.losses);
        holder.points.setText("Pont: " + item.points);
    }

    @Override
    public int getItemCount() {
        return standingsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView teamName, matches, results, points;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teamName = itemView.findViewById(R.id.textTeamName);
            matches = itemView.findViewById(R.id.textMatches);
            results = itemView.findViewById(R.id.textResults);
            points = itemView.findViewById(R.id.textPoints);
        }
    }
}
