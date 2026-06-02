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

        // 1. Initialisation des composants
        ImageView btnLogout = findViewById(R.id.btnLogout);
        MaterialButton btnWorkouts = findViewById(R.id.btnWorkouts);
        MaterialButton btnProfile = findViewById(R.id.btnProfile);
        TextView tvGreeting = findViewById(R.id.tvGreeting); // Notre texte
        TextView tvTotalSessions = findViewById(R.id.tvTotalSessions);
        TextView tvTotalTime = findViewById(R.id.tvTotalTime);

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

        if (userEmail != null) {
            // 1. On récupère les valeurs depuis SQLite
            int totalSessions = dbHelper.getReservationCount(userEmail);
            double totalHours = dbHelper.getTotalReservedTime(userEmail);

            // 2. On affiche les valeurs
            tvTotalSessions.setText(String.valueOf(totalSessions));

            // Format pour n'afficher qu'un seul chiffre après la virgule (ex: 1.5h)
            tvTotalTime.setText(String.format(java.util.Locale.getDefault(), "%.1fh", totalHours));
        }
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

    }
}