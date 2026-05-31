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
![Status](https://img.shields.io/badge/Status-En%20développement-orange?style=for-the-badge)

<br/>

> *Moderniser l'expérience sportive — une séance à la fois.*

</div>

---

## 🎯 À propos du projet

**PulseFit** est une application Android native conçue pour répondre aux besoins de gestion moderne d'un club sportif. Elle offre une expérience utilisateur fluide, esthétique et interactive, en alliant performance technique et design haut de gamme.

Le projet vise à **digitaliser et autonomiser** l'expérience des membres d'un club sportif grâce à trois axes majeurs :

| Axe | Description |
|-----|-------------|
| 📅 **Digitalisation des séances** | Consultation, réservation et suivi en temps réel |
| 🧭 **Interface intuitive** | Standards Material Design pour une navigation fluide |
| 🏅 **Engagement utilisateur** | Statistiques, badges de progression et Dark Mode |

---

## ⚙️ Architecture Technique

L'application repose sur une architecture en **trois couches** assurant une séparation claire des responsabilités :

```
┌─────────────────────────────────────────┐
│          PRESENTATION LAYER             │
│  Login · Dashboard · Profil · Workouts  │
│       Adapters (RecyclerView)           │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│            BUSINESS LAYER               │
│   Session Management (SharedPrefs)      │
│   Authentification persistante          │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│              DATA LAYER                 │
│   SQLite — Utilisateurs · Réservations  │
│            Historique des séances       │
└─────────────────────────────────────────┘
```

---

## 📋 Fonctionnalités Implémentées

### 🔐 Authentification
Gestion complète des comptes avec persistance de session sécurisée via `SharedPreferences`.
- Inscription / Connexion / Déconnexion
- Session persistante entre les lancements

### 🏠 Tableau de Bord
Vue dynamique calculant en temps réel via des requêtes SQL :
- Nombre de séances réservées
- Temps d'entraînement cumulé

### 🏋️ Gestion des Séances
- Liste dynamique avec `RecyclerView`
- Filtrage par type d'entraînement
- Vue détaillée par séance

### 📆 Système de Réservation
- Popup de confirmation interactive
- Prévention automatique des **réservations en double**

### 👤 Profil Membre
- Mise à jour des informations personnelles
- Suivi dynamique des statistiques physiques (poids / taille / IMC)

---

## 🛠️ Stack Technologique

| Catégorie | Technologie |
|-----------|-------------|
| **Langage** | Java |
| **UI / UX** | Material Components · Glassmorphism · Dark UI |
| **Base de données** | SQLite (local) |
| **Navigation** | Android Intent System |
| **Outils** | Android Studio · Database Inspector |

---

## 🗺️ Roadmap

### ✅ Réalisé
- [x] Architecture et structure du projet
- [x] Système d'authentification complet
- [x] Base de données SQLite (Utilisateurs & Séances)
- [x] Dashboard dynamique avec calculs en temps réel
- [x] Système de réservation avec anti-doublons

### 🔜 En cours / À venir
- [ ] 🗺️ Intégration Google Maps pour la géolocalisation
- [ ] 💬 Communication Coach (Intents Implicites)
- [ ] 🔔 Notifications push

---

## 🚀 Installation & Lancement

### Prérequis
- Android Studio (Hedgehog ou plus récent)
- SDK Android **API 34+**
- JDK 17+

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

> ⚠️ **Note :** Assurez-vous qu'un émulateur Android (API 34+) est configuré ou qu'un appareil physique est connecté en mode débogage USB.

---

## 📁 Structure du Projet

```
PulseFit/
├── app/
│   ├── src/main/
│   │   ├── java/
│   │   │   └── com.pulsefit/
│   │   │       ├── activities/      # Login, Dashboard, Profil, Workouts
│   │   │       ├── adapters/        # RecyclerView Adapters
│   │   │       ├── database/        # SQLite Helper & DAOs
│   │   │       └── models/          # User, Session, Reservation
│   │   └── res/
│   │       ├── layout/              # Fichiers XML des écrans
│   │       ├── drawable/            # Assets graphiques
│   │       └── values/              # Thèmes, couleurs, strings
│   └── build.gradle
└── README.md
```

---

## 🤝 Contribution

Les contributions sont les bienvenues ! Pour proposer des améliorations :

1. Forkez le projet
2. Créez votre branche (`git checkout -b feature/ma-fonctionnalite`)
3. Committez vos changements (`git commit -m 'feat: ajout de ma fonctionnalité'`)
4. Poussez la branche (`git push origin feature/ma-fonctionnalite`)
5. Ouvrez une **Pull Request**

---

<div align="center">

Fait avec ❤️ et ☕ — *PulseFit, votre club dans votre poche.*

</div>
