package com.example.pulsefit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pulsefit.R;
import com.example.pulsefit.activities.LandingActivity;
import com.example.pulsefit.database.DatabaseHelper;
import com.example.pulsefit.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import java.util.concurrent.TimeUnit;
import com.example.pulsefit.workers.MigrationWorker;

public class MainActivity extends AppCompatActivity {

    private ImageView btnLogout;
    private MaterialButton btnWorkouts, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activer le mode plein écran (Edge-to-Edge)
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.activity_main);

        // Schedule Automated History Migration
        PeriodicWorkRequest migrationRequest = new PeriodicWorkRequest.Builder(MigrationWorker.class, 1, TimeUnit.HOURS).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "HistoryMigrationWork",
                ExistingPeriodicWorkPolicy.KEEP,
                migrationRequest
        );

        // 1. Initialisation des composants
        ImageView btnLogout = findViewById(R.id.btnLogout);
        MaterialButton btnWorkouts = findViewById(R.id.btnWorkouts);
        MaterialButton btnProfile = findViewById(R.id.btnProfile);
        TextView tvGreeting = findViewById(R.id.tvGreeting); // Notre texte
        TextView tvTotalSessions = findViewById(R.id.tvTotalSessions);
        TextView tvTotalCalories = findViewById(R.id.tvTotalCalories);

        // 2. Récupérer les infos de l'utilisateur
        SessionManager session = new SessionManager(this);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        String userEmail = session.getUserEmail();
        if (userEmail != null) {
            String fullName = dbHelper.getUserName(userEmail);
            if (fullName != null && !fullName.isEmpty()) {
                // Petite astuce : on ne garde que le premier mot (le prénom) pour plus de convivialité
                String prenom = fullName.split(" ")[0];
                tvGreeting.setText("Prêt à transpirer, " + prenom + " ?");
            }
        }
        
        TextView tvAiQuote = findViewById(R.id.tvAiQuote);
        String[] quotes = {
            "Le succès commence par la discipline. Chaque séance vous rapproche de votre objectif.",
            "Ne vous arrêtez pas quand ça fait mal, arrêtez-vous quand c'est fini.",
            "Votre corps peut presque tout supporter. C'est votre esprit que vous devez convaincre.",
            "L'échec n'est qu'une opportunité de recommencer plus intelligemment.",
            "La seule mauvaise séance d'entraînement est celle qui n'a pas eu lieu."
        };
        int randomIndex = new java.util.Random().nextInt(quotes.length);
        tvAiQuote.setText("\"" + quotes[randomIndex] + "\"");

        updateDashboardStats();
        // Déconnexion
        // Déconnexion
        // Action : Déconnexion
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. On vide la mémoire de manière forcée
                SessionManager session = new SessionManager(MainActivity.this);
                session.logoutUser();

                // 2. Message de confirmation
                Toast.makeText(MainActivity.this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();

                // 3. Retour à la page d'accueil (ou de login)
                Intent intent = new Intent(MainActivity.this, LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Bouton Entraînements
        btnWorkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WorkoutsActivity.class);
                startActivity(intent);
            }
        });

        // Bouton Profil
        // Action : Mon Profil
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        
        MaterialButton btnLocation = findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LocationActivity.class);
            startActivity(intent);
        });

        MaterialButton btnLeaderboard = findViewById(R.id.btnLeaderboard);
        btnLeaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Force migration and stat update so user sees it instantly when returning
        updateDashboardStats();
    }

    private void updateDashboardStats() {
        SessionManager session = new SessionManager(this);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String userEmail = session.getUserEmail();

        if (userEmail != null) {
            // Force an immediate migration on resume so tests work perfectly!
            dbHelper.migratePastReservations();

            int totalSessions = dbHelper.getReservationCount(userEmail);
            int totalCalories = dbHelper.getTotalCaloriesBurned(userEmail);

            TextView tvTotalSessions = findViewById(R.id.tvTotalSessions);
            TextView tvTotalCalories = findViewById(R.id.tvTotalCalories);

            tvTotalSessions.setText(String.valueOf(totalSessions));
            tvTotalCalories.setText(String.valueOf(totalCalories));
        }
    }
}