package com.example.pulsefit.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulsefit.R;
import com.example.pulsefit.adapters.WorkoutAdapter;
import com.example.pulsefit.database.DatabaseHelper;
import com.example.pulsefit.models.Workout;
import com.example.pulsefit.utils.SessionManager;

import java.util.List;

public class WorkoutsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private List<Workout> workoutList;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager; // Pour avoir l'email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_workouts);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewWorkouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workoutList = dbHelper.getAllWorkouts();

        // Initialisation de l'adaptateur avec la gestion du clic
        adapter = new WorkoutAdapter(workoutList, new WorkoutAdapter.OnWorkoutClickListener() {
            @Override
            public void onPlayClick(Workout workout) {
                afficherPopupReservation(workout); // On appelle la méthode de la popup
            }
        });
        recyclerView.setAdapter(adapter);
    }

    // Méthode pour afficher la confirmation de réservation
    // Méthode pour afficher la confirmation de réservation (Version Premium)
    private void afficherPopupReservation(Workout workout) {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this, R.style.PulseFitDialogTheme)
                .setTitle("Réserver cette séance")
                .setMessage("Voulez-vous bloquer votre place pour : " + workout.getTitle() + " ?")
                .setPositiveButton("OUI, RÉSERVER", (dialog, which) -> {
                    String email = sessionManager.getUserEmail();
                    if (email != null) {
                        boolean isReserved = dbHelper.insertReservation(email, workout.getId());

                        if (isReserved) {
                            Toast.makeText(WorkoutsActivity.this, "🎉 Réservation confirmée !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WorkoutsActivity.this, "⚠️ Vous avez déjà réservé cette séance.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("ANNULER", null)
                .show();
    }
}