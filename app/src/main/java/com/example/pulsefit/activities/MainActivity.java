package com.example.pulsefit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pulsefit.R;
import com.example.pulsefit.activities.LandingActivity;
import com.example.pulsefit.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

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

        btnLogout = findViewById(R.id.btnLogout);
        btnWorkouts = findViewById(R.id.btnWorkouts);
        btnProfile = findViewById(R.id.btnProfile);

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
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Ouverture du profil...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}