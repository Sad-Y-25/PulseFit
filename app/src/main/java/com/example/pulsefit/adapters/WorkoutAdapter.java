package com.example.pulsefit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulsefit.R;
import com.example.pulsefit.models.Workout;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private List<Workout> workoutList;
    private OnWorkoutClickListener listener; // L'écouteur de clics

    // Interface pour gérer les clics en dehors de l'adaptateur
    public interface OnWorkoutClickListener {
        void onPlayClick(Workout workout);
    }

    public WorkoutAdapter(List<Workout> workoutList, OnWorkoutClickListener listener) {
        this.workoutList = workoutList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workoutList.get(position);
        holder.tvTitle.setText(workout.getTitle());
        holder.tvDetails.setText(workout.getDetails());

        // Quand on clique, on envoie l'objet Workout à l'activité
        holder.btnPlayContainer.setOnClickListener(v -> {
            if(listener != null) listener.onPlayClick(workout);
        });
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

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