package com.example.pulsefit.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pulsefit.R;
import com.example.pulsefit.database.DatabaseHelper;
import com.example.pulsefit.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvProfileName, tvProfileEmail;
    private TextInputEditText etPoids, etTaille; // Ajout des champs
    private MaterialButton btnSaveProfile;

    private DatabaseHelper dbHelper;
    private String userEmail; // On garde l'email en mémoire pour la sauvegarde

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activer le mode plein écran
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.activity_profile);

        // Initialisation des vues
        btnBack = findViewById(R.id.btnBackProfile);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        etPoids = findViewById(R.id.etProfilePoids);     // Connexion avec le XML
        etTaille = findViewById(R.id.etProfileTaille);   // Connexion avec le XML
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Base de données et Session
        SessionManager session = new SessionManager(this);
        dbHelper = new DatabaseHelper(this);

        userEmail = session.getUserEmail();

        if (userEmail != null) {
            // 1. Charger le Nom et l'Email
            String fullName = dbHelper.getUserName(userEmail);
            tvProfileEmail.setText(userEmail);
            if (fullName != null && !fullName.isEmpty()) {
                tvProfileName.setText(fullName.toUpperCase());
            } else {
                tvProfileName.setText("MEMBRE PULSEFIT");
            }

            // 2. Charger les Statistiques (Poids et Taille)
            String[] stats = dbHelper.getUserStats(userEmail);
            if (stats != null && stats.length == 2) {
                etPoids.setText(stats[0]);  // Pré-remplit le poids
                etTaille.setText(stats[1]); // Pré-remplit la taille
            }
        }

        // Action : Bouton Retour
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Retour au Dashboard
            }
        });

        // Action : Bouton Sauvegarder
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userEmail != null) {
                    // On récupère ce que l'utilisateur vient de taper
                    String nouveauPoids = etPoids.getText().toString().trim();
                    String nouvelleTaille = etTaille.getText().toString().trim();

                    // On envoie la mise à jour à SQLite
                    boolean isUpdated = dbHelper.updateUserStats(userEmail, nouveauPoids, nouvelleTaille);

                    if (isUpdated) {
                        Toast.makeText(ProfileActivity.this, "Statistiques mises à jour avec succès !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Erreur lors de la sauvegarde", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}