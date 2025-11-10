# 🏦 ClientRest - Application Android de Gestion de Comptes

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Retrofit](https://img.shields.io/badge/Retrofit-SQUARE-%23F15A2A?style=for-the-badge&logo=square)

Une application Android moderne servant de client pour une API REST de gestion de comptes bancaires. Ce projet démontre l'utilisation de **Retrofit** pour la communication réseau et la capacité de parser dynamiquement des données aux formats **JSON** et **XML**.

## ✨ Fonctionnalités

*   **Affichage des Comptes** : Visualisez tous les comptes dans une liste claire et élégante avec un design moderne.
*   **Ajout de Compte** : Créez de nouveaux comptes via une boîte de dialogue intuitive.
*   **Modification & Suppression** : Mettez à jour les informations d'un compte ou supprimez-le directement depuis la liste.
*   **Sélecteur de Format** : Basculez à la volée entre les formats de données `JSON` et `XML` pour voir la flexibilité des convertisseurs Retrofit.

## 📸 Aperçu de l'Application

Les interfaces illustrent les principales fonctionnalités de l'application, avec un design soigné et une expérience utilisateur fluide.

<img width="428" height="951" alt="Screenshot 2025-11-10 211631" src="https://github.com/user-attachments/assets/82c6a55d-3b7a-447f-a2d5-6315e1ec9c25" />


<img width="485" height="554" alt="Screenshot 2025-11-10 211949" src="https://github.com/user-attachments/assets/7042ed57-b757-476f-9328-941d9618ab5e" />


<img width="484" height="552" alt="Screenshot 2025-11-10 211942" src="https://github.com/user-attachments/assets/be4ae499-810b-401d-879b-0c07b589e318" />

## 🛠️ Technologies Utilisées

*   **Langage** : Java ☕
*   **Framework** : Android SDK natif
*   **Composants d'Interface** : Google Material Components
*   **Communication Réseau** : Retrofit 2
*   **Parsing de Données** :
    *   `Gson` pour le format JSON
    *   `SimpleXML` pour le format XML
*   **Architecture** : Approche Repository pour la séparation des préoccupations (UI / Données).

## 🚀 Mise en Route

Pour exécuter ce projet sur votre machine, suivez ces étapes :

1.  **Clonez le dépôt** :
    ```sh
    git clone https://github.com/votre-nom-utilisateur/ClientRest.git
    ```

2.  **Ouvrez dans Android Studio** :
    Importez ou ouvrez le projet cloné avec Android Studio.

3.  **⚠️ Configuration Cruciale du Backend** :
    Avant de lancer l'application, vous **devez** configurer l'adresse IP de votre serveur backend.

    *   Ouvrez le fichier : `app/src/main/java/ma/projet/restclient/config/RetrofitClient.java`
    *   Modifiez la constante `BASE_URL` avec l'adresse IP de la machine qui héberge votre API :

    ```java
    // REMPLACEZ par l'adresse IP de votre machine
    private static final String BASE_URL = "http://192.168.x.x:8082/";
    ```

4.  **Lancez l'Application** :
    Compilez et exécutez l'application sur un émulateur ou un appareil Android. Assurez-vous que votre appareil peut accéder à l'adresse réseau de votre serveur.

## 📸 Demonstration

https://github.com/user-attachments/assets/6ff17e13-a475-4403-a395-7d0402b33cea


---
