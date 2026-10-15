## 🌐🇧🇷 [Portuguese Version of README](README.md)
## 🌐🇺🇸 [English Version of README](README_EN.md)

# ♠️ TexasHold-ES – Texas Hold'em Poker API

RESTful API developed in **Java 21** and **Spring Boot** for managing Texas Hold'em poker matches, covering secure player registration, open table discovery, player seat joining, game initialization with rule enforcement, and random official card deck dealing.

---

## 🔨 Project Features

- **US01 – Player Registration (`POST /poker/cadastro`):** Visitor sign-up with bean validation, unique email constraint, and BCrypt one-way password hashing.
- **US09 – Open Tables Consultation (`GET /poker/partidasAbertas`):** Retrieve available tables open for seating, displaying current player count and maximum capacity.
- **US10 – Joining a Match (`POST /poker/{idPartida}/entrar`):** Validations for joining a table (capacity limits, table open status, duplicate player prevention).
- **US12 – Starting a Match (`POST /poker/{idPartida}/iniciar`):** Validation of creator/host authorization and minimum requirement of at least 2 seated players.
- **US13 – Card Distribution:** Generation of an official 52-card deck, cryptographic card shuffling with `SecureRandom`, and automatic dealing of 2 private hole cards per player upon match start.

---

## 📸 Visual Overview

<div align="center">
  <img src="https://raw.githubusercontent.com/tandpfun/skill-icons/main/icons/Java-Dark.svg" width="60" alt="Java 21" style="margin: 8px;">
  <img src="https://raw.githubusercontent.com/tandpfun/skill-icons/main/icons/Spring-Dark.svg" width="60" alt="Spring Boot" style="margin: 8px;">
  <img src="https://raw.githubusercontent.com/tandpfun/skill-icons/main/icons/PostgreSQL-Dark.svg" width="60" alt="PostgreSQL" style="margin: 8px;">
</div>

---

## ✔️ Techniques and Technologies Used

- **Java 21 LTS** (Eclipse Temurin)
- **Spring Boot 4.1.x**
  - **Spring Web** (RESTful Controllers)
  - **Spring Data JPA** (Relational ORM & Repositories)
  - **Spring Security** (BCrypt Password Encoder)
  - **Spring Validation** (Bean Validation / Hibernate Validator)
- **PostgreSQL 18** (Production database hosted via Coolify with active TLS/SSL)
- **H2 Database** (Isolated in-memory database for testing)
- **Lombok** (Boilerplate code reduction)
- **JUnit 5 & Mockito** (Unit tests and integration test suites with MockMvc)
- **Maven** (Dependency and build management)

---

## 📊 Application Flow Diagram

```mermaid
flowchart TD
    Player([Visitor / Player]) -->|POST /poker/cadastro| API[TexasHold-ES API]
    API -->|Hash Password & Save| DB[(PostgreSQL 18 SSL)]
    
    Player -->|GET /poker/partidasAbertas| API
    API -->|Fetch Status: WAITING| DB
    
    Player -->|POST /poker/{id}/entrar| API
    API -->|Validate Capacity & Player| DB
    
    Host([Host / Table Creator]) -->|POST /poker/{id}/iniciar| API
    API -->|Validate Host & Min. 2 Players| Rules[Poker Rules Engine]
    Rules -->|BaralhoService 52 Cards + SecureRandom| Cards[Deal 2 Cards per Player]
    Cards -->|Update Status: IN_PROGRESS| DB
```

---

## 📁 Project Structure

- **`src/main/java/com/projeto/texasholdes/`**
  - **`controller/`**: RESTful API endpoints.
  - **`service/`**: Domain services, card deck management, match orchestration.
  - **`repository/`**: Spring Data JPA repository layer.
  - **`entity/`**: Database entities (`Jogador`, `Partida`, etc.).
  - **`dto/`**: Request and response transfer models decoupled from persistence.
  - **`exception/`**: Global exception handler returning standardized error JSONs.
  - **`config/`**: Security configuration and application beans.
- **`src/main/resources/`**
  - `application.properties`: Active configuration targeting PostgreSQL with SSL.
- **`src/test/`**
  - Automated unit and integration test suites using H2.
- **`DATABASE.md`**: Infrastructure guide and connection strings.
- **`ARCHITECTURE.md`**: Technical architecture specification.

---

## 🛠️ How to Run the Project

### Prerequisites:
- **Java 21 (JDK)** installed (check with `java -version`).
- Internet access for downloading Maven dependencies.

### 1. Clone the Repository:
```bash
git clone <REPOSITORY_URL>
cd TexasHold-ES
```

### 2. Run the Automated Tests:
```bash
# Windows
.\mvnw test

# Linux / Mac
./mvnw test
```

### 3. Start the Application:
```bash
# Windows
.\mvnw spring-boot:run

# Linux / Mac
./mvnw spring-boot:run
```

The API will be available at: `http://localhost:8080`

---

## 🌐 Deployment

The database is live and fully accessible:
- **Host / Domain:** `painel.lacrosstech.com.br`
- **Port:** `15432` (Coolify TCP Proxy)
- **SSL:** Enforced (`sslmode=require`)

For production deployment in a Docker container:
```bash
# Package the compiled JAR
./mvnw clean package -DskipTests

# Run container with environment variables
java -jar target/texasholdes-0.0.1-SNAPSHOT.jar \
  --DB_URL=jdbc:postgresql://painel.lacrosstech.com.br:15432/postgres?sslmode=require \
  --DB_USERNAME=postgres \
  --DB_PASSWORD=YOUR_PASSWORD
```
