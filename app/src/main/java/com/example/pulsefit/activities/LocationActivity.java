package com.example.pulsefit.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pulsefit.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView btnBackMap;
    private GoogleMap mMap;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                if (fineLocationGranted != null && fineLocationGranted) {
                    enableMyLocation();
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    enableMyLocation();
                } else {
                    Toast.makeText(this, "Localisation refusée", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.activity_location);

        btnBackMap = findViewById(R.id.btnBackMap);
        btnBackMap.setOnClickListener(v -> finish());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style_dark));
            if (!success) {
                Log.e("LocationActivity", "Style parsing failed.");
            }
        } catch (Exception e) {
            Log.e("LocationActivity", "Can't find style. Error: ", e);
        }

        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        checkLocationPermission();

        // Fitness Park markers in Paris
        LatLng fpRepublique = new LatLng(48.8672, 2.3653); // Fitness Park République
        LatLng fpAlesia = new LatLng(48.8285, 2.3275); // Fitness Park Alésia
        LatLng fpBatignolles = new LatLng(48.8895, 2.3168); // Fitness Park Batignolles
        LatLng fpBagnolet = new LatLng(48.8601, 2.4005); // Fitness Park Bagnolet

        mMap.addMarker(new MarkerOptions().position(fpRepublique).title("Fitness Park République"));
        mMap.addMarker(new MarkerOptions().position(fpAlesia).title("Fitness Park Alésia"));
        mMap.addMarker(new MarkerOptions().position(fpBatignolles).title("Fitness Park Batignolles"));
        mMap.addMarker(new MarkerOptions().position(fpBagnolet).title("Fitness Park Bagnolet"));

        // Center on Paris
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.8566, 2.3522), 12f));
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            requestPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }
}
