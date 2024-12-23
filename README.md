# MyPlayerr

MyPlayerr est une application de gestion de musique MP3 simple et efficace, développée en JavaFX. Cette application permet de parcourir vos playlists, albums, chansons et artistes tout en profitant d'une interface utilisateur conviviale.

---

## Fonctionnalités

1. **Gestion de la musique**

   - Affichage des playlists, albums, chansons et artistes.
   - Ajout automatique des fichiers MP3 depuis un dossier spécifié.
   - Extraction des métadonnées (titre, artiste, album, durée) à partir des fichiers MP3.

2. **Navigation intuitive**

   - Menu vertical pour accéder rapidement aux sections : Playlists, Albums, Chansons, Artistes.
   - Menu horizontal pour accéder aux préférences et à l'aide.

3. **Base de données intégrée**

   - Sauvegarde des informations musicales dans une base SQLite.

---

## Prérequis

- **Java** : Version 21 ou plus (Liberica Full JDK recommandé pour inclure JavaFX).
- **Maven** : Utilisé pour la gestion des dépendances et la compilation.
- **yt-dlp** : Pour télécharger des fichiers MP3 depuis des vidéos YouTube.
- **FFmpeg** : Requis pour extraire et convertir l'audio en MP3.

---

## Installation

1. Clonez le dépôt GitHub :

   ```bash
   git clone https://github.com/username/MyPlayerr.git
   cd MyPlayerr
   ```

2. Installez les outils requis :

   - **yt-dlp** :
      1. Téléchargez `yt-dlp.exe` depuis [yt-dlp Releases](https://github.com/yt-dlp/yt-dlp/releases).
      2. Placez le fichier dans `C:/MyPlayerr/`.

   - **FFmpeg** :
      1. Téléchargez FFmpeg depuis [FFmpeg Downloads](https://ffmpeg.org/download.html).
      2. Extrayez les fichiers et copiez l'exécutable `ffmpeg.exe` dans `C:/MyPlayerr/`.

3. Créez les répertoires requis :

   - `C:/MyPlayerr/` : Répertoire principal pour les dépendances et les exécutables.
   - `C:/MyPlayerr/music/` : Répertoire où seront téléchargés les fichiers MP3.

4. Configurez les chemins dans l'application :

   - Dans les préférences de l'application, spécifiez `C:/MyPlayerr/music/` comme dossier par défaut pour les fichiers MP3.

5. Compilez et exécutez le projet :

   ```bash
   mvn clean javafx:run
   ```

---

## Utilisation

1. Lancez l'application.
2. Naviguez à travers les sections en utilisant le menu à gauche.
3. Ajoutez un dossier de musique en allant dans **Preferences > Change path music**.
4. Téléchargez des fichiers MP3 depuis YouTube en entrant l'URL dans la section dédiée.
5. Explorez vos fichiers MP3 triés par playlists, albums, artistes et chansons.

---

## Contribuer

Les contributions sont les bienvenues !

1. Forkez le projet.
2. Créez une branche de fonctionnalité :
   ```bash
   git checkout -b nouvelle-fonctionnalite
   ```
3. Faites vos modifications et committez :
   ```bash
   git commit -m "Ajout d'une nouvelle fonctionnalité"
   ```
4. Poussez la branche et créez une Pull Request.

---

Happy listening! 🎵
