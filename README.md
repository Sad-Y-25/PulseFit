<div align="center">

<br/>

```
██████╗ ██╗   ██╗██╗     ███████╗███████╗███████╗██╗████████╗
██╔══██╗██║   ██║██║     ██╔════╝██╔════╝██╔════╝██║╚══██╔══╝
██████╔╝██║   ██║██║     ███████╗█████╗  █████╗  ██║   ██║   
██╔═══╝ ██║   ██║██║     ╚════██║██╔══╝  ██╔══╝  ██║   ██║   
██║     ╚██████╔╝███████╗███████║███████╗██║     ██║   ██║   
╚═╝      ╚═════╝ ╚══════╝╚══════╝╚══════╝╚═╝     ╚═╝   ╚═╝   
```

### 📱 Application Mobile de Gestion de Club Sportif

![Android](https://img.shields.io/badge/Android-API%2034%2B-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-Native-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-Local%20DB-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![Material Design](https://img.shields.io/badge/Material%20Design-UI%2FUX-757575?style=for-the-badge&logo=materialdesign&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-Network-green?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Version%201.0-blue?style=for-the-badge)

<br/>

> *Moderniser l'expérience sportive — une séance à la fois.*

</div>

---

## 🎯 À propos du projet

**PulseFit** est une application Android native conçue pour répondre aux besoins de gestion moderne d'un club sportif (inspirée de grandes chaînes comme Fitness Park). Elle offre une expérience utilisateur fluide, esthétique et interactive, en alliant performance technique et design haut de gamme avec un système UI Dark Mode / Glassmorphism de bout en bout.

Le projet vise à **digitaliser et autonomiser** l'expérience des membres d'un club sportif grâce à trois axes majeurs :

| Axe | Description |
|-----|-------------|
| 📅 **Digitalisation des séances** | Consultation synchronisée, réservation et suivi de l'historique |
| 🧭 **Interface intuitive & Premium** | Standards Material Components, effet Glassmorphism, UI ultra-réactive |
| 🏅 **Gamification & Engagement** | Suivi dynamique des calories, Leaderboard, et Widget Home Screen |

---

## 📋 Fonctionnalités & Cas d'Utilisation (Use Cases)

### 1. 🔐 Authentification & Onboarding
- **Création de compte** : Inscription sécurisée stockée en base de données SQLite.
- **Setup Profil** : Configuration de l'utilisateur (nom, taille, poids) lors de la première connexion.
- **Connexion persistante** : Utilisation de `SharedPreferences` (SessionManager) pour garder l'utilisateur connecté.

### 2. 🏠 Tableau de Bord (Dashboard)
- **Suivi en temps réel** : Affiche les compteurs de séances réservées et de **calories totales brûlées**.
- **Quote motivationnelle** : Un message de motivation pour engager le membre.
- **Navigation Rapide** : Accès instantané aux modules principaux (Réservation, Profil, Classement, Carte).

### 3. 🏋️ Gestion des Entraînements (Networking + Local)
- **Synchronisation API (Retrofit)** : Les séances disponibles sont simulées via une API distante (avec Interceptor Retrofit) et synchronisées comme Single Source of Truth vers SQLite (`TABLE_SESSIONS`).
- **Consultation avec ViewPager2** : Deux onglets fluides : "Sessions Disponibles" et "Mes Réservations".
- **Réservation intelligente** : Empêche les doublons de réservation.

### 4. 🏆 Gamification (Leaderboard)
- Un système de **Classement des Membres** basé sur le nombre de calories brûlées.
- Top 3 mis en évidence visuellement avec un design Premium.

### 5. 👤 Espace Profil Interactif
- **Identité Numérique (QR Code)** : Génération automatique d'un QR Code unique pour l'accès physique au club.
- **Photo de profil** : Prise de photo intégrée via l'API Camera d'Android et stockage local avec sauvegarde du path en DB.
- **Assistance rapide** : Boutons d'action via **Intents Implicites** pour appeler le support ou envoyer un e-mail à l'équipe.

### 6. 🗺️ Géolocalisation (Google Maps)
- **Localisation des clubs** : Intégration complète de l'API Google Maps avec thème **Dark Mode Map**.
- Affiche des marqueurs réels pour différentes succursales (ex: Fitness Park République, Alésia, etc.).
- Permet de se repérer et de centrer la carte sur la position de l'utilisateur (permissions de localisation incluses).

### 7. ⚙️ Tâches en Arrière-Plan (WorkManager & AlarmManager)
- **Historisation automatique** : Un `MigrationWorker` (WorkManager) tourne toutes les heures en arrière-plan pour transférer automatiquement les séances expirées (passées) depuis les réservations vers `TABLE_HISTORY` et incrémenter les calories brûlées.
- **Notifications de rappel** : Utilisation de `AlarmManager` et de `BroadcastReceiver` pour planifier des notifications 1 heure avant le début d'une séance réservée.

### 8. 📱 Widget Android
- Un widget d'écran d'accueil (`PulseFitWidgetProvider`) affichant un récapitulatif ultra-rapide des informations clés sans ouvrir l'application.

---

## ⚙️ Architecture Technique

L'application repose sur une architecture robuste MVC/MVVM hybride et respecte la séparation des préoccupations :

```
┌─────────────────────────────────────────┐
│          PRESENTATION LAYER             │
│  Activities · Fragments · ViewPager2    │
│  Material Components & Glassmorphism    │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│            BUSINESS LAYER               │
│   WorkManager (MigrationWorker)         │
│   Retrofit (Mock API + Interceptor)     │
│   SessionManager (SharedPreferences)    │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│              DATA LAYER                 │
│   SQLiteHelper (V6)                     │
│   tables: users, sessions, reservations,│
│           history                       │
└─────────────────────────────────────────┘
```

---

## 🛠️ Stack Technologique

| Catégorie | Technologie |
|-----------|-------------|
| **Langage** | Java |
| **UI / UX** | Material Components 1.11+, RecyclerView, ViewPager2 |
| **Génération Code** | ZXing (QR Code) |
| **Réseau** | Retrofit2, Gson |
| **Base de données** | SQLite (local) |
| **Asynchrone** | AsyncTask, WorkManager, AlarmManager |
| **Cartographie** | Google Play Services Maps |

---

## 🚀 Installation & Lancement

### Prérequis
- Android Studio (Hedgehog ou plus récent)
- SDK Android **API 34+**
- JDK 17+
- Une clé API Google Maps valide dans `AndroidManifest.xml` (actuellement fournie en dur pour le prototypage).

### Étapes

```bash
# 1. Cloner le dépôt
git clone [URL_DU_DEPOT]

# 2. Ouvrir dans Android Studio
#    File > Open > sélectionner le répertoire cloné

# 3. Synchroniser les dépendances Gradle
#    Clic sur "Sync Now" dans la barre de notification

# 4. Lancer l'application
#    Run > Run 'app' (émulateur ou appareil physique)
```

> ⚠️ **Note :** Assurez-vous qu'un émulateur Android (API 34+) est configuré avec les services Google Play, ou qu'un appareil physique est connecté en mode débogage USB pour tester la carte Google Maps.

---

## 📁 Structure du Projet

```
PulseFit/
├── app/
│   ├── src/main/
│   │   ├── java/
│   │   │   └── com.pulsefit/
│   │   │       ├── activities/      # Landing, Login, Dashboard, Profile, Leaderboard, Map
│   │   │       ├── adapters/        # SessionsAdapter, LeaderboardAdapter
│   │   │       ├── database/        # DatabaseHelper (SQLite)
│   │   │       ├── fragments/       # SessionsFragment
│   │   │       ├── models/          # User, Session, Reservation, LeaderboardUser
│   │   │       ├── network/         # RetrofitClient, ApiService, MockInterceptor
│   │   │       ├── receivers/       # SessionReminderReceiver
│   │   │       ├── utils/           # SessionManager
│   │   │       ├── widget/          # PulseFitWidgetProvider
│   │   │       └── workers/         # MigrationWorker
│   │   └── res/
│   │       ├── layout/              # XML Layouts (Glassmorphism UI)
│   │       ├── drawable/            # UI Assets (bg_auth, bg_pulsefit, etc.)
│   │       └── values/              # themes.xml, colors.xml (Dark Navy, Neon Green)
│   └── build.gradle.kts
└── README.md
```

---

<div align="center">

Fait avec ❤️ et ☕ — *PulseFit, votre club dans votre poche.*

</div>
