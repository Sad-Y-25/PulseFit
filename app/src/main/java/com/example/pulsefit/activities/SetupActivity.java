package com.example.pulsefit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pulsefit.R;
import com.example.pulsefit.database.DatabaseHelper;
import com.example.pulsefit.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SetupActivity extends AppCompatActivity {

    private TextInputEditText etPoids, etTaille;
    private MaterialButton btnFinish;
    private TextView tvSkip;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_setup);

        etPoids = findViewById(R.id.etSetupPoids);
        etTaille = findViewById(R.id.etSetupTaille);
        btnFinish = findViewById(R.id.btnSetupFinish);
        tvSkip = findViewById(R.id.tvSetupSkip);

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Action quand on clique sur "COMMENCER"
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String poids = etPoids.getText().toString().trim();
                String taille = etTaille.getText().toString().trim();
                String email = session.getUserEmail(); // On récupère l'email de la session !

                if (email != null) {
                    // On met à jour la base de données
                    dbHelper.updateUserStats(email, poids, taille);
                    Toast.makeText(SetupActivity.this, "Profil configuré !", Toast.LENGTH_SHORT).show();
                }

                allerAuDashboard();
            }
        });

        // Action quand on clique sur "Ignorer"
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allerAuDashboard(); // On passe sans rien sauvegarder
            }
        });
    }

    private void allerAuDashboard() {
        Intent intent = new Intent(SetupActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}