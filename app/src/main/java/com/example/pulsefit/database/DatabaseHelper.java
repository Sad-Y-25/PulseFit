package com.example.pulsefit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pulsefit.models.Workout;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PulseFit.db";
    private static final int DATABASE_VERSION = 5; // Version 5 : Ajout de la photo de profil

    // --- Table USERS ---
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_NOM = "nom";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_POIDS = "poids";
    private static final String COL_TAILLE = "taille";
    private static final String COL_PHOTO_URI = "photo_uri";

    // --- Table SESSIONS ---
    private static final String TABLE_SESSIONS = "sessions";
    private static final String COL_SESSION_ID = "id";
    private static final String COL_SESSION_TITRE = "titre";
    private static final String COL_SESSION_DUREE = "duree";
    private static final String COL_SESSION_DIFFICULTE = "difficulte";

    // --- Table RESERVATIONS (NOUVEAU) ---
    private static final String TABLE_RESERVATIONS = "reservations";
    private static final String COL_RES_ID = "id";
    private static final String COL_RES_EMAIL = "user_email";
    private static final String COL_RES_SESSION_ID = "session_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Création de la table Utilisateurs
        String createTableUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOM + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_POIDS + " TEXT, " +
                COL_TAILLE + " TEXT, " +
                COL_PHOTO_URI + " TEXT)";
        db.execSQL(createTableUsers);

        // 2. Création de la table Séances
        String createTableSessions = "CREATE TABLE " + TABLE_SESSIONS + " (" +
                COL_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SESSION_TITRE + " TEXT, " +
                COL_SESSION_DUREE + " TEXT, " +
                COL_SESSION_DIFFICULTE + " TEXT)";
        db.execSQL(createTableSessions);

        // 3. Création de la table Réservations
        String createTableReservations = "CREATE TABLE " + TABLE_RESERVATIONS + " (" +
                COL_RES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RES_EMAIL + " TEXT, " +
                COL_RES_SESSION_ID + " INTEGER)";
        db.execSQL(createTableReservations);

        // 4. Insertion des Séances par défaut (Pour que la liste ne soit pas vide au début)
        insererSeanceDefaut(db, "Force : Haut du corps", "45 min", "🔥 Intense");
        insererSeanceDefaut(db, "Cardio HIIT & Abdos", "30 min", "⚡ Explosif");
        insererSeanceDefaut(db, "Mobilité & Étirements", "20 min", "🧘 Calme");
        insererSeanceDefaut(db, "Leg Day : Puissance", "60 min", "🦍 Brutal");
        insererSeanceDefaut(db, "Core Challenge", "15 min", "🔥 Intense");
        insererSeanceDefaut(db, "Full Body Express", "25 min", "⚡ Explosif");
    }

    private void insererSeanceDefaut(SQLiteDatabase db, String titre, String duree, String difficulte) {
        ContentValues values = new ContentValues();
        values.put(COL_SESSION_TITRE, titre);
        values.put(COL_SESSION_DUREE, duree);
        values.put(COL_SESSION_DIFFICULTE, difficulte);
        db.insert(TABLE_SESSIONS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_PHOTO_URI + " TEXT;");
        }
    }

    // ==========================================
    // MÉTHODES UTILISATEURS (Déjà existantes)
    // ==========================================
    public boolean insertUser(String nom, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOM, nom);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = "";
        Cursor cursor = db.rawQuery("SELECT " + COL_NOM + " FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ?", new String[]{email});
        if (cursor.moveToFirst()) name = cursor.getString(0);
        cursor.close();
        return name;
    }

    public boolean updateUserStats(String email, String poids, String taille) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_POIDS, poids);
        values.put(COL_TAILLE, taille);
        int result = db.update(TABLE_USERS, values, COL_EMAIL + " = ?", new String[]{email});
        return result > 0;
    }

    public String[] getUserStats(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] stats = new String[]{"", ""};
        Cursor cursor = db.rawQuery("SELECT " + COL_POIDS + ", " + COL_TAILLE + " FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            stats[0] = cursor.getString(0) != null ? cursor.getString(0) : "";
            stats[1] = cursor.getString(1) != null ? cursor.getString(1) : "";
        }
        cursor.close();
        return stats;
    }

    public boolean updateUserPhoto(String email, String uri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PHOTO_URI, uri);
        int result = db.update(TABLE_USERS, values, COL_EMAIL + " = ?", new String[]{email});
        return result > 0;
    }

    public String getUserPhoto(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String uri = null;
        Cursor cursor = db.rawQuery("SELECT " + COL_PHOTO_URI + " FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            uri = cursor.getString(0);
        }
        cursor.close();
        return uri;
    }

    // ==========================================
    // NOUVELLE MÉTHODE : LECTURE DES SÉANCES
    // ==========================================
    public List<Workout> getAllWorkouts() {
        List<Workout> workoutList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SESSIONS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0); // On récupère l'ID
                String titre = cursor.getString(1);
                String duree = cursor.getString(2);
                String difficulte = cursor.getString(3);

                workoutList.add(new Workout(id, titre, duree + " • " + difficulte));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return workoutList;
    }


    // Vérifier si la réservation existe déjà
    public boolean checkReservationExists(String email, int sessionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE " + COL_RES_EMAIL + "=? AND " + COL_RES_SESSION_ID + "=?", new String[]{email, String.valueOf(sessionId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Ajouter une réservation
    public boolean insertReservation(String email, int sessionId) {
        if (checkReservationExists(email, sessionId)) {
            return false; // Déjà réservé !
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RES_EMAIL, email);
        values.put(COL_RES_SESSION_ID, sessionId);
        long result = db.insert(TABLE_RESERVATIONS, null, values);
        return result != -1;
    }

    // 1. Compter le nombre de réservations de l'utilisateur
    public int getReservationCount(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_RESERVATIONS + " WHERE " + COL_RES_EMAIL + " = ?", new String[]{email});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // 2. Calculer le temps total (en heures)
    public double getTotalReservedTime(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        // On fait une jointure (JOIN) pour lire la "duree" dans la table sessions grâce à l'ID de la réservation
        String query = "SELECT s.duree FROM sessions s INNER JOIN reservations r ON s.id = r.session_id WHERE r.user_email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        int totalMinutes = 0;
        if (cursor.moveToFirst()) {
            do {
                String dureeStr = cursor.getString(0); // Exemple: "45 min"
                try {
                    // On extrait uniquement les chiffres du texte (45)
                    String numberOnly = dureeStr.replaceAll("[^0-9]", "");
                    totalMinutes += Integer.parseInt(numberOnly);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        // On convertit les minutes en heures (ex: 90 min = 1.5h)
        return totalMinutes / 60.0;
    }
}