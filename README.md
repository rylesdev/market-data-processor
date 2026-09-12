# Market Data Processor

`market-data-processor` est une application de traitement de données de marché développée en Java avec Spring Boot.

Le projet permet d’ingérer des données financières depuis plusieurs sources, de les transformer en objets métier, de les transmettre via Kafka, puis de les persister dans une base PostgreSQL.

## Fonctionnalités

L’application permet actuellement de :

- recevoir des `MarketData` via une API REST ;
- recevoir plusieurs `MarketData` dans une même requête JSON ;
- importer des fichiers JSON ou CSV ;
- surveiller un dossier local avec `WatchService` ;
- traiter plusieurs fichiers en parallèle avec plusieurs threads ;
- parser les données vers des objets `MarketData` ;
- calculer des statistiques sur les données importées ;
- publier les données dans un topic Kafka ;
- consommer les messages Kafka ;
- sérialiser et désérialiser les messages en JSON ;
- persister les données reçues dans PostgreSQL ;
- consulter, modifier et supprimer les données via l’API REST.

## Architecture

Le traitement principal suit ce flux :

```text
API REST / Fichier JSON ou CSV
        ↓
Spring Boot
        ↓
Parsing / traitement
        ↓
MarketDataProducer
        ↓
Kafka - topic market-data
        ↓
MarketDataConsumer
        ↓
MarketDataService
        ↓
PostgreSQL
```

Pour les fichiers locaux :

```text
Dossier input
    ↓
WatchService
    ↓
Controller
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

## Stack technique

- Java 25
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Kafka
- Apache Kafka
- PostgreSQL
- Hibernate
- Jackson
- Maven
- Docker
- JUnit
- Angular *(frontend prévu / en cours)*

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

L’API peut également recevoir plusieurs valeurs :

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

Chaque `MarketData` est publié comme un message Kafka indépendant.

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

## Kafka

Le projet utilise un topic :

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

Le projet accepte actuellement les formats :

```text
.csv
.json
```

Les fichiers locaux placés dans le dossier surveillé sont détectés automatiquement avec `WatchService`.

Chaque fichier est traité dans son propre thread afin qu’un fichier temporairement verrouillé ou lent à traiter ne bloque pas les autres fichiers.

## Base de données

Les données consommées depuis Kafka sont persistées dans PostgreSQL via Spring Data JPA et Hibernate.

La table principale est :

```text
market_data
```

## Objectifs du projet

Ce projet a été réalisé pour approfondir plusieurs aspects du développement d’applications Java :

- architecture en couches ;
- développement d’API REST ;
- traitement de fichiers ;
- programmation concurrente ;
- communication asynchrone avec Kafka ;
- sérialisation JSON ;
- persistance avec JPA/Hibernate ;
- intégration avec PostgreSQL ;
- organisation d’une application Spring Boot complète.

## Évolutions prévues

Les prochaines évolutions incluent notamment :

- développement d’une interface Angular ;
- visualisation des données de marché ;
- formulaires de création et de modification ;
- upload de fichiers depuis l’interface ;
- amélioration de la gestion des erreurs ;
- tests supplémentaires ;
- amélioration du monitoring du pipeline.

## Lancement

Prérequis :

```text
Java 25
Maven
PostgreSQL
Docker
```

Kafka peut être lancé avec Docker Compose :

```bash
docker compose up -d
```

Puis lancer l’application Spring Boot :

```bash
mvn spring-boot:run
```

## Configuration

Les informations sensibles doivent être fournies via des variables d’environnement.

Exemple :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/market_data_processor
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Il est recommandé de ne jamais versionner de mot de passe réel dans le repository.
