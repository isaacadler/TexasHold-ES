# Project Architecture

## 📁 Project Structure

The project follows a layered architecture (Controller-Service-Repository-Entity) powered by Spring Boot and Java 21, adhering to Clean Code, SOLID, and DRY principles.

```
TexasHold-ES/
├── .mvn/
│   └── wrapper/
│       ├── maven-wrapper.jar
│       └── maven-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/com/projeto/texasholdes/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   └── PokerController.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   └── CadastroJogadorRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── CadastroJogadorResponse.java
│   │   │   │       ├── OperacaoPartidaResponse.java
│   │   │   │       └── PartidaAbertaResponse.java
│   │   │   ├── entity/
│   │   │   │   ├── Carta.java
│   │   │   │   ├── Jogador.java
│   │   │   │   ├── Partida.java
│   │   │   │   └── StatusPartida.java
│   │   │   ├── exception/
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository/
│   │   │   │   ├── JogadorRepository.java
│   │   │   │   └── PartidaRepository.java
│   │   │   ├── service/
│   │   │   │   ├── BaralhoService.java
│   │   │   │   ├── JogadorService.java
│   │   │   │   └── PartidaService.java
│   │   │   └── TexasholdesApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/projeto/texasholdes/
│       │   ├── controller/
│       │   │   └── PokerControllerTest.java
│       │   ├── service/
│       │   │   ├── BaralhoServiceTest.java
│       │   │   ├── JogadorServiceTest.java
│       │   │   └── PartidaServiceTest.java
│       │   └── TexasholdesApplicationTests.java
│       └── resources/
│           └── application.properties
├── DATABASE.md
├── pom.xml
├── README.md
└── README_EN.md
```

### Directory Roles:
- **`controller/`**: Thin presentation layer exposing REST endpoints, validating input contracts, and handling HTTP statuses.
- **`dto/`**: Data Transfer Objects (Requests & Responses) separating internal entities from public API schemas.
- **`service/`**: Core business domain logic, validation orchestrations, card deck generation, and shuffling algorithms.
- **`entity/`**: JPA persistence entities and domain value objects (`Jogador`, `Partida`, `Carta`, `StatusPartida`).
- **`repository/`**: Spring Data JPA abstraction layer for database querying.
- **`exception/`**: Centralized exception handling returning standardized error responses with HTTP status codes.
- **`config/`**: Security settings, bean definitions, and password hashing (`BCryptPasswordEncoder`).
- **`test/`**: Unit and integration test suites covering happy and edge cases with H2 in-memory isolation and Mockito.

---

## 🎯 Architecture Principles

1. **Single Responsibility Principle (SRP):** Controllers only route requests, Services solely execute business operations, and Repositories handle persistence.
2. **Layered Separation & Inversion of Control (IoC):** Dependencies are injected through interfaces and abstractions using Spring `@RequiredArgsConstructor` constructor injection.
3. **Decoupled Contracts via DTOs:** Entities are never directly exposed to API consumers, avoiding over-posting vulnerabilities and serialization loops.
4. **Resilient Security:** Sensitive user passwords are encrypted using one-way cryptographic hashing (BCrypt) before database writes.
5. **Environment Isolation:** Zero-config automated testing via embedded H2 database (`src/test/resources`), while development and production connect to remote PostgreSQL instances with SSL enforcement.

---

## 📝 Conventions

- **File & Class Naming:**
  - Classes and Interfaces: `PascalCase` (e.g., `PartidaService`, `JogadorRepository`).
  - Methods and Variables: `camelCase` (e.g., `listarPartidasAbertas`, `idPartida`).
  - Packages: lowercase, single words (e.g., `com.projeto.texasholdes.controller`).
  - Database Tables: lowercase, plural (e.g., `jogadores`, `partidas`).
- **RESTful Endpoints:**
  - Resources named in clear context (e.g., `/poker/cadastro`, `/poker/{idPartida}/entrar`).
  - Explicit HTTP verbs (`POST` for mutations, `GET` for retrieval).
- **Code Standards:** Clean Code, SOLID, DRY, and KISS guidelines. Files strictly constrained under 200–300 lines for high readability.

---

## 🛠️ Maintenance and Expansion

### Maintenance:
- **Dependency Updates:** Run `./mvnw versions:display-dependency-updates` periodically to monitor Spring Boot and plugin versions.
- **Continuous Quality:** Keep all unit and integration tests green by executing `./mvnw test` before every commit.
- **Database Schema Migrations:** Managed through Hibernate `ddl-auto=update` in development or dedicated Flyway/Liquibase scripts in high-availability environments.

### Adding New Features:
1. Define entities and relationships in `entity/` and write corresponding interfaces in `repository/`.
2. Create DTO request/response schemas with Bean Validation in `dto/`.
3. Implement business logic inside a focused service under `service/`.
4. Expose the endpoint in `PokerController` or create a new dedicated controller in `controller/`.
5. Add unit and integration tests in `src/test/` covering both happy path and edge constraints.

---

**Última atualização**: 15/10/2026  
**Versão do projeto**: 0.0.1-SNAPSHOT  
**Manutenedor**: Felipe Moreira
