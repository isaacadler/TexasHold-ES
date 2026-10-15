## 🌐🇧🇷 [Versão em Português do README](README.md)
## 🌐🇺🇸 [English Version of README](README_EN.md)

# ♠️ TexasHold-ES – API de Pôquer Texas Hold'em

API REST desenvolvida em **Java 21** e **Spring Boot** para gerenciamento de partidas de pôquer Texas Hold'em, contemplando cadastro seguro de jogadores, consulta de mesas abertas, controle de entrada de participantes, inicialização de partidas com validações de regras e distribuição aleatória de cartas de baralho oficial.

---

## 🔨 Funcionalidades do Projeto

- **US01 – Cadastro de Jogadores (`POST /poker/cadastro`):** Registro de visitantes com validação de dados, unicidade de e-mail e criptografia de senha via BCrypt.
- **US09 – Consulta de Partidas Abertas (`GET /poker/partidasAbertas`):** Listagem de mesas disponíveis para entrada com contagem de jogadores presentes e capacidade máxima.
- **US10 – Entrada em Partida (`POST /poker/{idPartida}/entrar`):** Validação de regras para entrada na mesa (capacidade máxima, partida aberta, duplicidade).
- **US12 – Início de Partida (`POST /poker/{idPartida}/iniciar`):** Validação de autorização do host/criador e número mínimo de 2 jogadores para início do jogo.
- **US13 – Distribuição de Cartas:** Geração de baralho de 52 cartas oficiais, embaralhamento criptográfico com `SecureRandom` e distribuição automática de 2 cartas fechadas por jogador ao iniciar a mesa.

---

## 📸 Exemplo Visual da API

<div align="center">
  <img src="https://raw.githubusercontent.com/tandpfun/skill-icons/main/icons/Java-Dark.svg" width="60" alt="Java 21" style="margin: 8px;">
  <img src="https://raw.githubusercontent.com/tandpfun/skill-icons/main/icons/Spring-Dark.svg" width="60" alt="Spring Boot" style="margin: 8px;">
  <img src="https://raw.githubusercontent.com/tandpfun/skill-icons/main/icons/PostgreSQL-Dark.svg" width="60" alt="PostgreSQL" style="margin: 8px;">
</div>

---

## ✔️ Técnicas e Tecnologias Utilizadas

- **Java 21 LTS** (Eclipse Temurin)
- **Spring Boot 4.1.x**
  - **Spring Web** (Controllers RESTful)
  - **Spring Data JPA** (Persistência relacional)
  - **Spring Security** (BCrypt Password Hashing)
  - **Spring Validation** (Bean Validation / Hibernate Validator)
- **PostgreSQL 18** (Banco de dados de produção hospedado via Coolify com SSL habilitado)
- **H2 Database** (Banco em memória isolado para suíte de testes automatizados)
- **Lombok** (Produtividade e redução de código boilerplate)
- **JUnit 5 & Mockito** (Testes unitários e testes de integração com MockMvc)
- **Maven** (Gerenciamento de dependências e build)

---

## 📊 Fluxo da Aplicação

```mermaid
flowchart TD
    Visitante([Visitante / Jogador]) -->|POST /poker/cadastro| API[API TexasHold-ES]
    API -->|Criptografa Senha & Persiste| DB[(PostgreSQL 18 SSL)]
    
    Visitante -->|GET /poker/partidasAbertas| API
    API -->|Consulta Mesas Status: AGUARDANDO| DB
    
    Visitante -->|POST /poker/{id}/entrar| API
    API -->|Valida Vagas & Jogador| DB
    
    Host([Jogador Criador / Host]) -->|POST /poker/{id}/iniciar| API
    API -->|Valida Criador & Min. 2 Jogadores| Regras[Regras do Jogo]
    Regras -->|BaralhoService 52 Cartas + SecureRandom| Cartas[Distribui 2 cartas por Jogador]
    Cartas -->|Atualiza Status: EM_ANDAMENTO| DB
```

---

## 📁 Estrutura do Projeto

- **`src/main/java/com/projeto/texasholdes/`**
  - **`controller/`**: Endpoints HTTP REST da API.
  - **`service/`**: Regras de negócio, baralho e orquestração de partidas.
  - **`repository/`**: Interfaces de acesso ao banco com Spring Data JPA.
  - **`entity/`**: Entidades mapeadas para o PostgreSQL (`Jogador`, `Partida`, etc.).
  - **`dto/`**: Modelos de requisição e resposta desacoplados do banco de dados.
  - **`exception/`**: Handler global para respostas de erro padronizadas.
  - **`config/`**: Configurações de segurança e beans da aplicação.
- **`src/main/resources/`**
  - `application.properties`: Configurações ativas do PostgreSQL com SSL.
- **`src/test/`**
  - Testes automatizados unitários e de integração com H2.
- **`DATABASE.md`**: Guia completo de credenciais e conexão com o banco de dados.
- **`ARCHITECTURE.md`**: Detalhamento da arquitetura técnica do projeto.

---

## 🛠️ Abrir e rodar o projeto

### Pré-requisitos:
- **Java 21 (JDK)** instalado (pode ser verificado com `java -version`).
- Conexão com a internet para dependências do Maven.

### 1. Clonar o Repositório:
```bash
git clone <URL_DO_REPOSITORIO>
cd TexasHold-ES
```

### 2. Executar os Testes Automatizados:
```bash
# Windows
.\mvnw test

# Linux / Mac
./mvnw test
```

### 3. Executar a Aplicação:
```bash
# Windows
.\mvnw spring-boot:run

# Linux / Mac
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## 🌐 Deploy

A infraestrutura do banco de dados já está hospedada e configurada:
- **Host / Domínio:** `painel.lacrosstech.com.br`
- **Porta:** `15432` (TCP Proxy Coolify)
- **SSL:** Obrigatório (`sslmode=require`)

Para deploy da API em container Docker no Coolify ou qualquer servidor:
```bash
# Gerar o pacote .jar compilado
./mvnw clean package -DskipTests
```
Executar o container passando as variáveis de ambiente:
```bash
java -jar target/texasholdes-0.0.1-SNAPSHOT.jar \
  --DB_URL=jdbc:postgresql://painel.lacrosstech.com.br:15432/postgres?sslmode=require \
  --DB_USERNAME=postgres \
  --DB_PASSWORD=SUA_SENHA
```
