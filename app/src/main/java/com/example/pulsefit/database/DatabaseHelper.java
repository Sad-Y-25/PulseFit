package com.example.pulsefit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
public class DatabaseHelper extends SQLiteOpenHelper {

    // Nom et version de la base de données
    private static final String DATABASE_NAME = "PulseFit.db";
    private static final int DATABASE_VERSION = 1;

    // --- Noms des tables ---
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_SESSIONS = "Sessions";
    private static final String TABLE_RESERVATIONS = "Reservations";
    private static final String TABLE_HISTORY = "History";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création de la table Users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT, " +
                "email TEXT, " +
                "password TEXT, " +
                "photo TEXT)";
        db.execSQL(createUsersTable);

        // Création de la table Sessions
        String createSessionsTable = "CREATE TABLE " + TABLE_SESSIONS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titre TEXT, " +
                "coach TEXT, " +
                "date TEXT, " +
                "places INTEGER)";
        db.execSQL(createSessionsTable);

        // Création de la table Reservations
        String createReservationsTable = "CREATE TABLE " + TABLE_RESERVATIONS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "sessionId INTEGER)";
        db.execSQL(createReservationsTable);

        // Création de la table History
        String createHistoryTable = "CREATE TABLE " + TABLE_HISTORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "activite TEXT, " +
                "duree TEXT, " +
                "calories INTEGER)";
        db.execSQL(createHistoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En cas de mise à jour, on supprime les anciennes tables et on les recrée
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    /**
     * Méthode pour inscrire un nouvel utilisateur (Register)
     */
    public boolean insertUser(String nom, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nom", nom);
        contentValues.put("email", email);
        contentValues.put("password", password);
        // La photo pourra être ajoutée plus tard via la fonction d'édition de profil
        contentValues.put("photo", "");

        // insert() retourne -1 s'il y a une erreur
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    /**
     * Méthode pour vérifier les identifiants lors de la connexion (Login)
     */
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // On cherche un utilisateur avec cet email ET ce mot de passe
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?", new String[]{email, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
    }
}