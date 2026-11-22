# Guia de Testes e Demonstração da API - TexasHold-ES

## 🧪 Resumo dos Testes Unitários Executados
- **Total de Testes:** 32 testes unitários e de integração
- **Falhas:** 0
- **Status:** `BUILD SUCCESS`
- **Classes de Testes:**
  - `StrategyPokerTest`: Teste unitário para cada possibilidade de combinação (Royal Flush, Straight Flush, Quadra, Full House, Flush, Straight, Trinca, Dois Pares, Um Par, Carta Alta).
  - `PokerFacadeTest`: Testes da Fachada delegando chamadas para o modelo.
  - `PokerControllerTest`: Testes dos endpoints HTTP integrados à Fachada.
  - `PartidaServiceTest`: Teste da apuração de pontos finais com Strategy.
  - `JogadorServiceTest`, `BaralhoServiceTest`, `TexasholdesApplicationTests`.

---

## 🎮 Roteiro de Demonstração e Execução da API

Para validação ou apresentação das operações de negócio expostas pela camada de controle (`PokerController` -> `PokerFacade`), foram disponibilizadas as requisições HTTP correspondentes ao ciclo de vida da partida:

### 1. Inicialização da Aplicação
```powershell
.\mvnw spring-boot:run
```

### 2. Cadastrar Jogadores (US01)
- **Método / Rota:** `POST http://localhost:8080/poker/cadastro`
- **Cabeçalho:** `Content-Type: application/json`
- **Payloads (JSON):**
  - Jogador 1:
    ```json
    {
      "nome": "Felipe",
      "login": "felipe",
      "senha": "senha123"
    }
    ```
  - Jogador 2:
    ```json
    {
      "nome": "Adler",
      "login": "adler",
      "senha": "senha123"
    }
    ```
- **Equivalente cURL (Terminal):**
  ```powershell
  curl.exe -X POST "http://localhost:8080/poker/cadastro" -H "Content-Type: application/json" -d '{\"nome\":\"Felipe\",\"login\":\"felipe\",\"senha\":\"senha123\"}'
  curl.exe -X POST "http://localhost:8080/poker/cadastro" -H "Content-Type: application/json" -d '{\"nome\":\"Adler\",\"login\":\"adler\",\"senha\":\"senha123\"}'
  ```
- **Retorno Esperado:** `HTTP 201 Created` contendo o ID gerado (ex: `{"id": 1, ...}`).

---

### 3. Criar Mesa / Partida
- **Método / Rota:** `POST http://localhost:8080/poker/criarPartida?idCriador=1&maxJogadores=8`
- **Equivalente cURL:**
  ```powershell
  curl.exe -X POST "http://localhost:8080/poker/criarPartida?idCriador=1&maxJogadores=8"
  ```
- **Retorno Esperado:** `HTTP 201 Created` (`{"idPartida": 1}`).

---

### 4. Consultar Mesas Abertas (US12)
- **Método / Rota:** `GET http://localhost:8080/poker/partidasAbertas`
- **Equivalente cURL:**
  ```powershell
  curl.exe -X GET "http://localhost:8080/poker/partidasAbertas"
  ```
- **Retorno Esperado:** `HTTP 200 OK` com array de mesas com status `CRIADA` e contagem de vagas.

---

### 5. Entrada do Segundo Jogador na Mesa (US09)
- **Método / Rota:** `POST http://localhost:8080/poker/1/entrar?idJogador=2`
- **Equivalente cURL:**
  ```powershell
  curl.exe -X POST "http://localhost:8080/poker/1/entrar?idJogador=2"
  ```
- **Retorno Esperado:** `HTTP 200 OK` (deduz o cacife de 20 fichas e associa o participante à mesa).

---

### 6. Iniciar Partida e Distribuir Cartas (US10 & US13)
- **Método / Rota:** `POST http://localhost:8080/poker/1/iniciar?idJogadorResponsavel=1`
- **Equivalente cURL:**
  ```powershell
  curl.exe -X POST "http://localhost:8080/poker/1/iniciar?idJogadorResponsavel=1"
  ```
- **Retorno Esperado:** `HTTP 200 OK` (distribui 2 cartas fechadas para cada jogador e 5 cartas na mesa).

---

### 7. Apurar Vencedor e Contabilizar Pontos via Strategy
- **Método / Rota:** `POST http://localhost:8080/poker/1/apurar`
- **Parâmetro Opcional:** `?estrategia=PARES` ou `?estrategia=FLUSH` (permite direcionar a avaliação de uma regra específica).
- **Equivalente cURL:**
  ```powershell
  curl.exe -X POST "http://localhost:8080/poker/1/apurar"
  ```
- **Exemplo de Retorno:**
  ```json
  {
    "idPartida": 1,
    "estrategiaAplicada": "RegraPokerStrategy",
    "cartasMesa": [
      {"naipe": "COPAS", "valor": "AS"},
      {"naipe": "COPAS", "valor": "REI"},
      {"naipe": "OUROS", "valor": "SETE"},
      {"naipe": "ESPADAS", "valor": "DOIS"},
      {"naipe": "PAUS", "valor": "NOVE"}
    ],
    "vencedores": [
      {
        "idJogador": 1,
        "nomeJogador": "Felipe",
        "pontuacao": 100000,
        "descricaoMao": "Full House",
        "cartasJogador": [
          {"naipe": "COPAS", "valor": "DAMA"},
          {"naipe": "COPAS", "valor": "VALETE"}
        ]
      }
    ]
  }
  ```

