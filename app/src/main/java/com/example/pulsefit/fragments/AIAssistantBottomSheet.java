package com.example.pulsefit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pulsefit.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Random;

public class AIAssistantBottomSheet extends BottomSheetDialogFragment {

    private TextView tvDynamicTip;
    private TextView tvRecommendedSession;

    private final String[] tips = {
            "Buvez au moins 2L d'eau aujourd'hui pour optimiser votre récupération musculaire.",
            "N'oubliez pas de vous étirer après l'entraînement pour éviter les courbatures.",
            "La régularité bat l'intensité. Maintenez le cap !",
            "Un bon sommeil de 7 à 8 heures est le meilleur complément alimentaire.",
            "Variez vos entraînements pour surprendre vos muscles et progresser plus vite."
    };

    private final String[] recommendations = {
            "Yoga Zen - Idéal pour votre récupération d'aujourd'hui !",
            "CrossFit Intense - Parfait pour repousser vos limites.",
            "Pilates Core - Renforcez votre sangle abdominale en douceur.",
            "Boxe Thaï - Défoulez-vous et brûlez un maximum de calories !",
            "Full Body Express - Vous avez peu de temps ? Cette session est pour vous."
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_ai, container, false);

        tvDynamicTip = view.findViewById(R.id.tvDynamicTip);
        tvRecommendedSession = view.findViewById(R.id.tvRecommendedSession);

        generateAIContent();

        return view;
    }

    private void generateAIContent() {
        Random random = new Random();
        
        String randomTip = tips[random.nextInt(tips.length)];
        String randomRecommendation = recommendations[random.nextInt(recommendations.length)];

        tvDynamicTip.setText(randomTip);
        tvRecommendedSession.setText(randomRecommendation);
    }
}
