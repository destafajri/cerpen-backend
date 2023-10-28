# Cerpen Application

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.4-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13.3-blue)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-8.10.2-blue)
![Docker Compose](https://img.shields.io/badge/Docker%20Compose-3.9-blue)

This is a Cerpen (short story) application built with Java Spring Boot for the backend. The application includes two roles: Admin and Author and implements JWT (JSON Web Tokens) for authentication and authorization. The technology stack includes Java 17, Spring Boot, PostgreSQL for the database, Elasticsearch for text search, and Docker Compose for containerization.

You can access the project [here](https://java-springboot-cerpen.onrender.com/).
## Table of Contents

- [Database Schema](#database-schema)
- [Getting Started](#getting-started)
- [Authentication](#authentication)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Database Schema

Here's an overview of the database schema for this project:

### Users Table

| Column Name | Data Type  | Constraints            |
|-------------|------------|------------------------|
| id          | UUID       | PRIMARY KEY            |
| email       | VARCHAR(255) | NOT NULL, UNIQUE      |
| username    | VARCHAR(255) | NOT NULL, UNIQUE      |
| password    | VARCHAR(255) | NOT NULL              |
| role        | VARCHAR(255) | NOT NULL              |
| is_active   | BOOLEAN    | NOT NULL              |
| created_at  | TIMESTAMP  | NOT NULL              |
| updated_at  | TIMESTAMP  |                        |


### Admin Table

| Column Name | Data Type  | Constraints         |
|-------------|------------|---------------------|
| id          | UUID       | PRIMARY KEY         |
| user_id     | UUID       | NOT NULL, UNIQUE    |
| name        | VARCHAR(255) | NOT NULL          |

### Author Table

| Column Name | Data Type  | Constraints         |
|-------------|------------|---------------------|
| id          | UUID       | PRIMARY KEY         |
| user_id     | UUID       | NOT NULL, UNIQUE    |
| name        | VARCHAR(255) | NOT NULL          |

### Cerpen Table

| Column Name    | Data Type    | Constraints         |
|----------------|--------------|---------------------|
| id             | UUID         | PRIMARY KEY         |
| author_id      | UUID         | NOT NULL            |
| title          | VARCHAR(255) | NOT NULL, UNIQUE    |
| tema           | VARCHAR(255) | NOT NULL            |
| cerpen_contains | TEXT        | NOT NULL, UNIQUE    |
| is_active   | BOOLEAN    | NOT NULL              |
| created_at     | TIMESTAMP    | NOT NULL            |
| updated_at     | TIMESTAMP    |                     |

## Getting Started

To get started with this project, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/destafajri/java-springboot-cerpen-backend
    ```
2. Set up the database (PostgreSQL and Elasticsearch) and configure the application.yml.

3. Build and run the application.

