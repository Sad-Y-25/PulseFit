package com.example.pulsefit.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulsefit.R;
import com.example.pulsefit.adapters.WorkoutAdapter;
import com.example.pulsefit.models.Workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private List<Workout> workoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activer le mode immersif
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.activity_workouts);

        // 1. Configurer le bouton retour
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Ferme cette page et retourne au Dashboard
            }
        });

        // 2. Initialiser le RecyclerView
        recyclerView = findViewById(R.id.recyclerViewWorkouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Créer des fausses données
        workoutList = new ArrayList<>();
        workoutList.add(new Workout("Force : Haut du corps", "45 min • 🔥 Intense"));
        workoutList.add(new Workout("Cardio HIIT & Abdos", "30 min • ⚡ Explosif"));
        workoutList.add(new Workout("Mobilité & Étirements", "20 min • 🧘 Calme"));
        workoutList.add(new Workout("Leg Day : Puissance", "60 min • 🦍 Brutal"));
        workoutList.add(new Workout("Core Challenge", "15 min • 🔥 Intense"));
        workoutList.add(new Workout("Full Body Express", "25 min • ⚡ Explosif"));

        // 4. Connecter l'adaptateur
        adapter = new WorkoutAdapter(workoutList);
        recyclerView.setAdapter(adapter);
    }
}