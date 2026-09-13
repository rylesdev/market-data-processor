# Market Data Processor

`market-data-processor` est une application de traitement de données de marché développée autour de Java, Spring Boot, Kafka, PostgreSQL et Angular.

Le projet permet d’ingérer des données financières depuis plusieurs sources, de les transformer en objets `MarketData`, de les transmettre via Kafka, puis de les persister dans PostgreSQL. Une interface Angular permet également d’utiliser les principales fonctionnalités de l’application.

## Fonctionnalités

L’application permet actuellement de :

- recevoir un ou plusieurs `MarketData` via une API REST ;
- consulter un `MarketData` par identifiant ;
- modifier et supprimer un `MarketData` ;
- importer des fichiers JSON ou CSV depuis l’interface Angular ;
- surveiller un dossier local avec `WatchService` ;
- traiter plusieurs fichiers en parallèle avec plusieurs threads ;
- parser les données vers des objets `MarketData` ;
- calculer des statistiques sur les données importées ;
- publier les données dans un topic Kafka ;
- consommer les messages Kafka ;
- sérialiser et désérialiser les messages en JSON ;
- persister les données reçues dans PostgreSQL ;
- gérer les états de chargement, de succès et d’erreur côté Angular.

## Architecture

Le flux principal depuis l’interface est :

```text
Angular
   ↓
API REST Spring Boot
   ↓
MarketDataProducer
   ↓
Kafka - topic market-data
   ↓
MarketDataConsumer
   ↓
MarketDataService
   ↓
Spring Data JPA / Hibernate
   ↓
PostgreSQL
```

Pour l’import de fichiers :

```text
Fichier CSV / JSON
      ↓
Upload Angular ou dossier input
      ↓
Controller / WatchService
      ↓
Thread dédié au fichier
      ↓
Tache
      ↓
Reader
      ↓
Parser CSV / JSON
      ↓
List<MarketData>
      ↓
Kafka
      ↓
PostgreSQL
```

## Structure du projet

```text
market-data-processor/
├── backend/
│   ├── src/
│   └── pom.xml
├── frontend/
├── docker-compose.yml
├── .gitignore
└── README.md
```

## Stack technique

### Backend

- Java 25
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Kafka
- Hibernate
- Jackson
- Maven
- JUnit

### Messaging et données

- Apache Kafka
- PostgreSQL
- Docker / Docker Compose

### Frontend

- Angular
- TypeScript
- HTML
- CSS

## Modèle de données

Un `MarketData` représente une donnée de marché avec notamment :

```text
symbole
date
prix
volume
```

Exemple :

```json
{
  "symbole": "BNP",
  "date": "2026-09-12T19:15:00",
  "prix": 76.42,
  "volume": 12500
}
```

L’API peut également recevoir plusieurs valeurs dans une même requête :

```json
[
  {
    "symbole": "MSFT",
    "date": "2026-09-12T20:15:00",
    "prix": 418.37,
    "volume": 9200
  },
  {
    "symbole": "TSLA",
    "date": "2026-09-12T20:16:30",
    "prix": 247.81,
    "volume": 15300
  }
]
```

Chaque `MarketData` envoyé dans le pipeline Kafka est publié comme un message indépendant.

## API REST

Base URL :

```text
http://localhost:8080/market-data
```

Principaux endpoints :

```text
POST   /market-data
GET    /market-data/{id}
PUT    /market-data/{id}
DELETE /market-data/{id}

POST   /market-data/files
```

`POST /market-data/files` permet d’envoyer un fichier CSV ou JSON dans le pipeline de traitement.

## Frontend Angular

L’interface Angular permet actuellement de :

- saisir plusieurs `MarketData` puis les envoyer au backend ;
- rechercher un `MarketData` par identifiant ;
- modifier un `MarketData` ;
- supprimer un `MarketData` ;
- importer un fichier CSV ou JSON ;
- afficher les messages de chargement, de succès et d’erreur.

Le frontend communique avec l’API Spring Boot sur :

```text
http://localhost:8080/market-data
```

## Kafka

Le projet utilise le topic :

```text
market-data
```

Le Producer publie les objets `MarketData` avec leur symbole comme clé Kafka.

Exemple :

```text
key   = BNP
value = MarketData
```

Les objets sont sérialisés avec le `JsonSerializer` de Spring Kafka puis reconstruits côté Consumer avec `JsonDeserializer`.

Le Consumer appartient au groupe :

```text
market-data-group
```

## Traitement des fichiers

Les formats acceptés sont :

```text
.csv
.json
```

Les fichiers peuvent être envoyés depuis l’interface Angular ou placés dans le dossier local surveillé.

Les fichiers locaux sont détectés avec `WatchService`. Chaque fichier est traité dans son propre thread afin qu’un fichier temporairement verrouillé ou lent à traiter ne bloque pas le traitement des autres fichiers.

## Base de données

Les données consommées depuis Kafka sont persistées dans PostgreSQL via Spring Data JPA et Hibernate.

La table principale est :

```text
market_data
```

## Gestion du projet

Le suivi des tâches et des différentes étapes du projet est disponible sur Trello :

[Voir le tableau Trello - Market Data Processor](https://trello.com/b/eJhHrjus/market-data-processor)

## Objectifs du projet

Ce projet a été réalisé pour approfondir plusieurs aspects du développement d’applications :

- architecture en couches ;
- développement d’API REST avec Spring Boot ;
- traitement de fichiers ;
- programmation concurrente ;
- communication asynchrone avec Kafka ;
- sérialisation JSON ;
- persistance avec JPA / Hibernate ;
- intégration avec PostgreSQL ;
- développement d’une interface Angular ;
- communication entre frontend et backend.

## Améliorations prévues

Les prochaines étapes concernent principalement la fiabilisation et la finition du projet :

- améliorer la validation des données et les réponses HTTP ;
- renforcer la gestion des erreurs côté backend et frontend ;
- améliorer l’ergonomie des formulaires Angular ;
- ajouter des tests complémentaires ;
- rendre la gestion des fichiers invalides et verrouillés plus robuste ;
- nettoyer la configuration, les logs et le code avant finalisation.

## Lancement

### Prérequis

```text
Java 25
Maven
Node.js / npm
Angular CLI
PostgreSQL
Docker
```

### Kafka

Depuis la racine du projet :

```bash
docker compose up -d
```

### Backend

Depuis le dossier `backend` :

```bash
mvn spring-boot:run
```

Le backend est disponible sur :

```text
http://localhost:8080
```

### Frontend

Depuis le dossier `frontend` :

```bash
npm install
ng serve
```

Le frontend est disponible sur :

```text
http://localhost:4200
```

## Configuration

Le fichier `application.properties` contient la configuration locale de l’application et n’est pas versionné dans le repository.
