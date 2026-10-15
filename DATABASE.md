# 🗄️ Informações de Conexão - PostgreSQL (TexasHold-ES)

## 📌 Visão Geral do Banco

- **Nome da Instância (Coolify):** `postgresql-database-5ai1fjhxmlysyoqllwutqdx3`
- **Imagem:** `postgres:18-alpine` (PostgreSQL 18.6)
- **Host / Domínio:** `painel.lacrosstech.com.br`
- **IP VPS:** `192.227.211.229`
- **Porta Pública (TCP Proxy):** `15432`
- **Porta Interna Docker:** `5432`
- **Port Mapping:** `3000:5432`
- **Usuário:** `postgres`
- **Banco Inicial:** `postgres`
- **Senha:** `VKvQQuft8tY77OVQZAtXTbYcK3FpzVQOhqWptYQZw3sHkGUFLs9SJu4F10jBVpF5`

---

## 🔒 Certificado & Segurança (SSL)

- **Status SSL:** `Enabled`
- **Modo SSL:** `require (secure)`
- **Validade do Certificado:** até 15/10/2027 às 02:54:35
- **Acesso Público:** `Public through TCP proxy` (porta `15432`)

---

## 🔗 Strings de Conexão Prontas

### 1. Acesso Público via Domínio (Recomendado para Dev Local, DBeaver, VS Code)
```text
postgres://postgres:VKvQQuft8tY77OVQZAtXTbYcK3FpzVQOhqWptYQZw3sHkGUFLs9SJu4F10jBVpF5@painel.lacrosstech.com.br:15432/postgres?sslmode=require
```

**Formato JDBC (Spring Boot / Java):**
```properties
spring.datasource.url=jdbc:postgresql://painel.lacrosstech.com.br:15432/postgres?sslmode=require
spring.datasource.username=postgres
spring.datasource.password=VKvQQuft8tY77OVQZAtXTbYcK3FpzVQOhqWptYQZw3sHkGUFLs9SJu4F10jBVpF5
```

---

### 2. Acesso Público via IP Direto
```text
postgres://postgres:VKvQQuft8tY77OVQZAtXTbYcK3FpzVQOhqWptYQZw3sHkGUFLs9SJu4F10jBVpF5@192.227.211.229:15432/postgres?sslmode=require
```

---

### 3. Acesso Interno Docker (Deploy da API dentro do mesmo Coolify)
Quando a aplicação for empacotada em container e executada no mesmo host do Coolify:
```text
postgres://postgres:VKvQQuft8tY77OVQZAtXTbYcK3FpzVQOhqWptYQZw3sHkGUFLs9SJu4F10jBVpF5@5ai1fjhxmlysyoqllwutqdx3:5432/postgres?sslmode=require
```

**Variáveis de Ambiente para Container:**
```env
DB_URL=jdbc:postgresql://5ai1fjhxmlysyoqllwutqdx3:5432/postgres?sslmode=require
DB_USERNAME=postgres
DB_PASSWORD=VKvQQuft8tY77OVQZAtXTbYcK3FpzVQOhqWptYQZw3sHkGUFLs9SJu4F10jBVpF5
```

---

## 🛠️ Como Conectar em Ferramentas Visuais (DBeaver, pgAdmin, DataGrip)

1. **Host:** `painel.lacrosstech.com.br` (ou `192.227.211.229`)
2. **Porta:** `15432`
3. **Database:** `postgres`
4. **Username:** `postgres`
5. **Password:** `VKvQQuft8tY77OVQZAtXTbYcK3FpzVQOhqWptYQZw3sHkGUFLs9SJu4F10jBVpF5`
6. **Aba SSL:** Marcar como ativo e modo `require` (ou adicionar parâmetro `sslmode=require`).
