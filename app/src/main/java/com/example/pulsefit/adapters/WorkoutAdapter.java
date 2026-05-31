package com.example.pulsefit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulsefit.R;
import com.example.pulsefit.models.Workout;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private List<Workout> workoutList;

    public WorkoutAdapter(List<Workout> workoutList) {
        this.workoutList = workoutList;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // C'est ici qu'on charge ton design item_workout.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workoutList.get(position);

        // On injecte les textes dans le design
        holder.tvTitle.setText(workout.getTitle());
        holder.tvDetails.setText(workout.getDetails());

        // Action quand on clique sur le bouton Play
        holder.btnPlayContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Démarrage : " + workout.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    // Cette classe lie les IDs du XML au code Java
    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDetails;
        MaterialCardView btnPlayContainer;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvWorkoutTitle);
            tvDetails = itemView.findViewById(R.id.tvWorkoutDetails);
            btnPlayContainer = itemView.findViewById(R.id.btnPlayContainer);
        }
    }
}