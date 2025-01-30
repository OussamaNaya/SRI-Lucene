# 🔎 Moteur de Recherche Lucene

![image](https://github.com/user-attachments/assets/96d7be0e-df18-4b56-9661-bbb8f84512f2)

**Moteur de Recherche Lucene** est une application web développée en **Java/JEE** qui utilise la bibliothèque **Lucene** pour indexer et rechercher des articles.

## 📌 Use Case

L'utilisateur interagit avec le moteur de recherche pour récupérer des fichiers (articles) déjà indexés. 
- À partir d'une requête, le moteur retourne les **5 articles les plus pertinents** (ce paramètre peut être modifié selon le besoin).
- L'utilisateur a la possibilité d'**afficher** ou **télécharger** les fichiers récupérés.

## 🏗️ Technologies et Bibliothèques utilisées

- **JDK 19.0.2**
- **Tomcat v9.0**
- **Lucene Core 2.9.4**
- **FontBox 2.0.32**
- **Commons Logging 1.3.4**
- **PDFBox 2.0.32**

## 🚀 Installation et Exécution

1. **Cloner le projet** :
   ```sh
   git clone https://github.com/OussamaNaya/SRI-Lucene.git
   cd MoteurDeRechercheLucene
   ```

2. **Configurer l'environnement** :
   - Installer **JDK 19.0.2**
   - Déployer l'application sur **Tomcat v9.0**
   - Ajouter les dépendances nécessaires dans le projet

3. **Lancer l’application** :
   - Démarrer **Tomcat**
   - Accéder à l'interface via `http://localhost:8080/Search.jsp`

## 📜 Licence
Ce projet est sous licence **MIT**. Vous êtes libre de l'utiliser et de le modifier.
