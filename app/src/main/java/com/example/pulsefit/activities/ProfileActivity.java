package com.example.pulsefit.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.google.android.material.card.MaterialCardView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pulsefit.R;
import com.example.pulsefit.database.DatabaseHelper;
import com.example.pulsefit.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private ImageView btnBack, ivProfilePhoto;
    private TextView tvProfileName, tvProfileEmail, tvMemberName;
    private FloatingActionButton fabTakePhoto;
    private MaterialButton btnSaveProfile, btnCallAssistance, btnMessageAssistance;

    private DatabaseHelper dbHelper;
    private String userEmail;

    // Camera Result Launcher
    private final ActivityResultLauncher<Void> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicturePreview(),
            result -> {
                if (result != null) {
                    ivProfilePhoto.setImageBitmap(result);
                    saveImageToInternalStorage(result);
                }
            }
    );

    // Permission Result Launcher
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    takePictureLauncher.launch(null);
                } else {
                    Toast.makeText(this, "Permission caméra refusée", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.activity_profile);

        btnBack = findViewById(R.id.btnBackProfile);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvMemberName = findViewById(R.id.tvMemberName);
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        fabTakePhoto = findViewById(R.id.fabTakePhoto);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnCallAssistance = findViewById(R.id.btnCallAssistance);
        btnMessageAssistance = findViewById(R.id.btnMessageAssistance);

        SessionManager session = new SessionManager(this);
        dbHelper = new DatabaseHelper(this);

        userEmail = session.getUserEmail();

        if (userEmail != null) {
            String fullName = dbHelper.getUserName(userEmail);
            tvProfileEmail.setText(userEmail);
            if (fullName != null && !fullName.isEmpty()) {
                tvProfileName.setText(fullName.toUpperCase());
                tvMemberName.setText(fullName);
            } else {
                tvProfileName.setText("MEMBRE PULSEFIT");
                tvMemberName.setText("MEMBRE PULSEFIT");
            }

            // Load Profile Photo
            String photoUri = dbHelper.getUserPhoto(userEmail);
            if (photoUri != null && !photoUri.isEmpty()) {
                File imgFile = new File(photoUri);
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivProfilePhoto.setImageBitmap(bitmap);
                }
            }
        }

        btnBack.setOnClickListener(v -> finish());
        
        MaterialCardView cvMemberCard = findViewById(R.id.cvMemberCard);
        cvMemberCard.setOnClickListener(v -> showQRCodeDialog());

        // Save Profile Action (Just a placeholder now as stats are removed, but can be reused)
        btnSaveProfile.setOnClickListener(v -> 
                Toast.makeText(ProfileActivity.this, "Profil enregistré !", Toast.LENGTH_SHORT).show()
        );

        // Take Photo Action
        fabTakePhoto.setOnClickListener(v -> checkCameraPermissionAndLaunch());

        // Contact Actions
        btnCallAssistance.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0123456789")); // Dummy Assistance Number
            startActivity(intent);
        });

        btnMessageAssistance.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:support@pulsefit.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Demande d'assistance - " + tvProfileName.getText());
            startActivity(intent);
        });
    }

    private void checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePictureLauncher.launch(null);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        File directory = getDir("profile_images", MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, "profile_" + System.currentTimeMillis() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            
            // Save path to DB
            if (userEmail != null) {
                dbHelper.updateUserPhoto(userEmail, mypath.getAbsolutePath());
                Toast.makeText(this, "Photo enregistrée !", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showQRCodeDialog() {
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "Erreur: Utilisateur non trouvé", Toast.LENGTH_SHORT).show();
            return;
        }

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_qr_code);
        
        // Ensure the dialog has a clean white background and fits well
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        ImageView ivQRCode = dialog.findViewById(R.id.ivQRCode);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            String qrContent = "PULSEFIT-ID: " + userEmail;
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrContent, BarcodeFormat.QR_CODE, 800, 800);
            ivQRCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la génération du QR Code", Toast.LENGTH_SHORT).show();
        }

        dialog.show();
    }
}