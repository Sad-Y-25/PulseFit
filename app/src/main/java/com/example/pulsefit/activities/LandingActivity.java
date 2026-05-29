package com.example.pulsefit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pulsefit.R;
import com.google.android.material.button.MaterialButton;

public class LandingActivity extends AppCompatActivity {

    private MaterialButton btnGoToRegister, btnGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Activer le mode immersif (Edge-to-Edge) pour que l'image passe sous la barre de statut
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        // 2. Lier au design XML
        setContentView(R.layout.activity_landing);

        // 3. Initialiser les boutons
        btnGoToRegister = findViewById(R.id.btnGoToRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        // 4. Action pour "J'AI DÉJÀ UN COMPTE"
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'écran de connexion
                Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // 5. Action pour "REJOINDRE LE CLUB"
        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'écran d'inscription
                Intent intent = new Intent(LandingActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}