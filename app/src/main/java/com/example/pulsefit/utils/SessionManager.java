package com.example.pulsefit.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    // Nom du fichier de préférences
    private static final String PREF_NAME = "PulseFitSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_EMAIL = "userEmail";

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Sauvegarder la session après une connexion réussie
    public void createLoginSession(String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_EMAIL, email);
        editor.apply(); // apply() enregistre en arrière-plan (très rapide)
    }

    // Vérifier si l'utilisateur est connecté
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Détruire la session (Déconnexion)
    // Détruire la session (Déconnexion)
    public void logoutUser() {
        editor.putBoolean(KEY_IS_LOGGED_IN, false); // On force la valeur à faux
        editor.remove(KEY_EMAIL); // On supprime l'email
        editor.commit(); // IMPORTANT : On utilise commit() qui efface instantanément au lieu de apply()
    }
}