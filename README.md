# 🚗 Sistema de Monitoramento de Ativos (SMA)

![Java](https://img.shields.io/badge/Java-17-orange?style=flat&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen?style=flat&logo=spring)
![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Modelo de Dados](#-modelo-de-dados)
- [Sistema de Segurança](#-sistema-de-segurança)
- [API Endpoints](#-api-endpoints)
- [Configuração e Instalação](#-configuração-e-instalação)
- [Uso](#-uso)
- [Fluxo de Teste](#-fluxo-de-teste)
- [Contribuição](#-contribuição)

---

## 🎯 Visão Geral

O **Sistema de Monitoramento de Ativos (SMA)** é uma plataforma backend robusta construída em Java com Spring Boot, projetada para **gerenciar e monitorar frotas de veículos em tempo real**.

O sistema foi desenvolvido com foco em **transporte público municipal**, permitindo:
- Monitoramento em tempo real de veículos (ônibus, vans, ambulâncias)
- Gestão completa de rotas, horários e viagens
- Controle de acesso granular baseado em roles (RBAC)
- Ingestão assíncrona de dados de GPS de dispositivos IoT
- API pública para consulta de posição de veículos

### 🎯 Casos de Uso

1. **Prefeituras e Órgãos Públicos**: Monitorar frotas de transporte público, ambulâncias e veículos escolares
2. **Empresas de Transporte**: Gerenciar rotas, horários e posicionamento de frotas
3. **Cidadãos**: Acompanhar em tempo real a localização de ônibus e linhas favoritas

---

## ✨ Funcionalidades

### 🔐 Sistema de Autenticação e Autorização
- **JWT (JSON Web Tokens)** para autenticação stateless
- **5 Roles distintas** com permissões granulares:
  - `ADMIN`: Acesso total ao sistema
  - `MANAGER`: Gerenciamento restrito aos seus departamentos
  - `DRIVER`: Controle de viagens e operações de motorista
  - `PASSENGER`: Acesso público + rotas favoritas
  - `SYSTEM`: Dispositivos IoT para ingestão de dados GPS

### 🏢 Gestão Organizacional
- Hierarquia de **Departamentos** e **Frotas**
- Gerentes com acesso restrito aos seus departamentos
- CRUD completo de ativos (veículos)
- Gerenciamento de dispositivos de rastreamento GPS

### 🚌 Sistema de Transporte Público
- Cadastro de **Rotas** com múltiplas **Paradas**
- Sistema de **Horários** (Schedules) com perfis de dias da semana
- **Viagens (Trips)** alocadas para motoristas e veículos específicos
- Motoristas podem iniciar/finalizar viagens via app

### 📡 Rastreamento em Tempo Real
- Ingestão assíncrona de dados GPS via eventos Spring (`@EventListener`, `@Async`)
- Armazenamento de histórico de localizações
- Status atual de cada ativo monitorado
- API pública para consulta de posição de veículos

### 👥 Portal do Cidadão
- Visualização pública de ativos em circulação
- Sistema de rotas favoritas para usuários autenticados
- Consulta de rotas, horários e paradas

---

## 🛠️ Tecnologias Utilizadas

### Core
- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA** (Hibernate)
- **Spring Security 6**
- **Maven**

### Banco de Dados
- **PostgreSQL** (Recomendado para produção)
- **H2** (Desenvolvimento e testes)

### Segurança
- **JWT** (JSON Web Tokens) - `jjwt 0.11.5`
- **BCrypt** para hash de senhas

### Utilitários
- **Lombok** - Redução de boilerplate
- **MapStruct 1.6.3** - Mapeamento de DTOs
- **Jakarta Validation** - Validação de dados

### Documentação
- **SpringDoc OpenAPI** (Swagger UI)

### Outras
- **Apache Commons CSV** - Exportação de relatórios
- **iText7** - Geração de PDFs
- **Bucket4j** - Rate limiting
- **Spring WebSocket** - Comunicação em tempo real
- **Spring Mail** - Envio de e-mails

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura **modular orientada a domínios** (Package-by-Domain), com separação clara entre camadas:

```
src/main/java/com/monitoramento/
├── asset/              # Domínio de Ativos (Veículos e Dispositivos)
│   ├── api/            # Controllers e DTOs
│   ├── domain/         # Models, Enums e Use Cases
│   └── infrastructure/ # Repositórios JPA
│
├── organization/       # Domínio Organizacional (Departamentos e Frotas)
│   ├── api/
│   ├── domain/
│   └── infrastructure/
│
├── tracking/           # Domínio de Rastreamento (GPS e Localização)
│   ├── api/
│   ├── domain/
│   └── infrastructure/
│
├── transit/            # Domínio de Transporte Público (Rotas, Horários, Viagens)
│   ├── api/
│   ├── domain/
│   └── infrastructure/
│
├── user/               # Domínio de Usuários (Autenticação e Autorização)
│   ├── api/
│   ├── domain/
│   └── infrastructure/
│
└── shared/             # Configurações e Utilitários Compartilhados
    ├── config/         # SecurityConfig, DataSeeder, GlobalExceptionHandler
    ├── security/       # JwtService, JwtAuthenticationFilter
    ├── manager/        # Endpoints específicos para gerentes
    └── pub/            # Endpoints públicos
```

### 📐 Padrões Arquiteturais

- **Clean Architecture**: Separação entre camadas de API, Domínio e Infraestrutura
- **Use Cases**: Lógica de negócio encapsulada em casos de uso
- **Repository Pattern**: Abstração da camada de persistência
- **DTO Pattern**: Separação entre entidades de domínio e representações externas
- **Mapper Pattern**: MapStruct para conversão entre entidades e DTOs

---

## 📂 Estrutura do Projeto

### Camadas do Domínio

Cada domínio segue a mesma estrutura:

#### 📍 `/api` - Camada de API
```
api/
├── controller/     # REST Controllers
├── dto/            # Data Transfer Objects (Request/Response)
└── mapper/         # MapStruct Mappers
```

#### 📍 `/domain` - Camada de Domínio
```
domain/
├── model/          # Entidades JPA
│   └── enums/      # Enumerações
├── service/        # Serviços de domínio
└── useCase/        # Casos de uso (regras de negócio)
```

#### 📍 `/infrastructure` - Camada de Infraestrutura
```
infrastructure/
└── persistence/    # Repositórios JPA
```

---

## 🗄️ Modelo de Dados

### Entidades Principais

#### 🏢 Organization (Organização)
- **Department**: Departamentos (ex: Secretaria de Saúde, Transporte)
- **Fleet**: Frotas pertencentes a departamentos

#### 🚗 Asset (Ativos)
- **MonitoredAsset**: Veículos/ativos rastreados
- **TrackingDevice**: Dispositivos GPS
- **VehicleDetails**: Detalhes específicos de veículos (placa, modelo, ano)

#### 📍 Tracking (Rastreamento)
- **LocationDataPoint**: Histórico de localizações GPS
- **AssetCurrentStatus**: Status atual de cada ativo

#### 🚌 Transit (Transporte Público)
- **Route**: Rotas de transporte
- **Stop**: Paradas (pontos de ônibus)
- **RouteStopAssignment**: Relação entre rotas e paradas (com ordem)
- **Schedule**: Perfis de horário (dias úteis, fim de semana)
- **ScheduleDeparture**: Horários de partida
- **Trip**: Viagens alocadas (liga rota + motorista + veículo + data)

#### 👤 User (Usuários)
- **User**: Usuários do sistema
- **Role**: Papéis/permissões
- **FavoriteRoute**: Rotas favoritas dos usuários
- **RefreshToken**: Tokens de atualização

### Relacionamentos Principais

```
Department (1) ──→ (N) Fleet
Fleet (1) ──→ (N) MonitoredAsset
MonitoredAsset (1) ←─→ (1) TrackingDevice
MonitoredAsset (1) ─→ (1) VehicleDetails
MonitoredAsset (1) ←─→ (N) LocationDataPoint
MonitoredAsset (1) ←─→ (1) AssetCurrentStatus

Route (1) ──→ (N) RouteStopAssignment ←── (1) Stop
Route (1) ──→ (N) Schedule
Schedule (1) ──→ (N) ScheduleDeparture

Trip (N) ──→ (1) ScheduleDeparture
Trip (N) ──→ (1) MonitoredAsset
Trip (N) ──→ (1) User (driver)

User (N) ←──→ (N) Role
User (N) ←──→ (N) Department (manageable)
User (N) ──→ (N) Route (favorites)
```

---

## 🔐 Sistema de Segurança

### Autenticação JWT

O sistema utiliza **JWT (JSON Web Tokens)** para autenticação stateless:

1. **Login**: `POST /api/v1/auth/login`
   - Envia `login` (CPF ou username) e `password`
   - Retorna `accessToken` e `refreshToken`

2. **Refresh Token**: `POST /api/v1/auth/refresh`
   - Envia o `refreshToken` para obter novo `accessToken`

3. **Logout**: `POST /api/v1/auth/logout`
   - Invalida o `refreshToken` do usuário

### Roles e Permissões

| Role | Descrição | Endpoints Principais |
|------|-----------|---------------------|
| **ADMIN** | Administrador com acesso total | `/api/v1/admin/**` |
| **MANAGER** | Gerente restrito aos seus departamentos | `/api/v1/manager/**` |
| **DRIVER** | Motorista (gerencia suas viagens) | `/api/v1/driver/**` |
| **PASSENGER** | Usuário/passageiro | `/api/v1/favorites/**` |
| **SYSTEM** | Dispositivos IoT | `/api/v1/tracking/ingest` |

### Configuração de Segurança

A configuração está em `SecurityConfig.java`:
- **CSRF**: Desabilitado (stateless)
- **Session**: Stateless (não mantém estado no servidor)
- **CORS**: Desabilitado (configurar para produção)
- **Password Encoder**: BCrypt

---

## 🌐 API Endpoints

Todas as rotas são prefixadas com `/api/v1`

### 🔑 Autenticação

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| `POST` | `/auth/login` | Pública | Fazer login |
| `POST` | `/auth/refresh` | Pública | Atualizar token |
| `POST` | `/auth/logout` | Autenticado | Fazer logout |

### 🌍 Endpoints Públicos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/public/assets/live` | Lista ativos públicos em tempo real |
| `GET` | `/public/routes` | Lista todas as rotas disponíveis |
| `GET` | `/public/routes/{id}` | Detalhes de uma rota |
| `GET` | `/public/routes/{id}/schedules` | Horários de uma rota |

### 👤 Usuário Autenticado

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/favorites/{routeId}` | Adicionar rota aos favoritos |
| `GET` | `/favorites/my-favorites` | Listar rotas favoritas |
| `DELETE` | `/favorites/{routeId}` | Remover rota dos favoritos |

### 🚗 Motorista (DRIVER)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/driver/trips/my-today` | Minhas viagens de hoje |
| `POST` | `/driver/trips/{tripId}/start` | Iniciar viagem |
| `POST` | `/driver/trips/{tripId}/end` | Finalizar viagem |

### 👨‍💼 Gerente (MANAGER)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/manager/fleet/status` | Status dos ativos dos meus departamentos |
| `POST` | `/manager/assets` | Criar novo ativo na minha frota |

### 📡 Sistema (SYSTEM)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/tracking/ingest` | Ingestão de dados GPS (dispositivos IoT) |

### 👑 Administrador (ADMIN)

#### Usuários
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/admin/users` | Criar usuário |
| `GET` | `/admin/users` | Listar usuários |
| `GET` | `/admin/users/{id}` | Buscar usuário |
| `PUT` | `/admin/users/{id}` | Atualizar usuário |
| `PUT` | `/admin/users/{id}/permissions` | Atualizar permissões |

#### Departamentos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/admin/departments` | Criar departamento |
| `GET` | `/admin/departments` | Listar departamentos |
| `PUT` | `/admin/departments/{id}` | Atualizar departamento |
| `DELETE` | `/admin/departments/{id}` | Deletar departamento |

#### Frotas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/admin/fleets` | Criar frota |
| `GET` | `/admin/fleets/{id}` | Buscar frota |
| `PUT` | `/admin/fleets/{id}` | Atualizar frota |
| `DELETE` | `/admin/fleets/{id}` | Deletar frota |
| `GET` | `/admin/fleets/by-department/{deptId}` | Listar frotas por departamento |

#### Ativos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/admin/assets` | Criar ativo |
| `GET` | `/admin/assets/{id}` | Buscar ativo |
| `PUT` | `/admin/assets/{id}` | Atualizar ativo |
| `DELETE` | `/admin/assets/{id}` | Deletar ativo |
| `GET` | `/admin/assets/{id}/vehicle-details` | Detalhes do veículo |
| `GET` | `/admin/assets/by-fleet/{fleetId}` | Listar ativos por frota |
| `POST` | `/admin/assets/{id}/assign-device` | Vincular dispositivo |
| `POST` | `/admin/assets/{id}/unassign-device` | Desvincular dispositivo |

#### Dispositivos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/admin/devices` | Criar dispositivo |
| `GET` | `/admin/devices` | Listar dispositivos |
| `PUT` | `/admin/devices/{id}` | Atualizar dispositivo |
| `DELETE` | `/admin/devices/{id}` | Deletar dispositivo |
| `GET` | `/admin/devices/unassigned` | Listar dispositivos não vinculados |

#### Transporte Público - Paradas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/admin/transit/stops` | Criar parada |
| `GET` | `/admin/transit/stops` | Listar paradas |
| `GET` | `/admin/transit/stops/{id}` | Buscar parada |
| `PUT` | `/admin/transit/stops/{id}` | Atualizar parada |
| `DELETE` | `/admin/transit/stops/{id}` | Deletar parada |

#### Transporte Público - Rotas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/admin/transit/routes` | Criar rota |
| `GET` | `/admin/transit/routes` | Listar rotas |
| `GET` | `/admin/transit/routes/{id}` | Buscar rota |
| `PUT` | `/admin/transit/routes/{id}` | Atualizar rota |
| `DELETE` | `/admin/transit/routes/{id}` | Deletar rota |
| `POST` | `/admin/transit/routes/{id}/stops` | Adicionar parada à rota |
| `DELETE` | `/admin/transit/routes/{routeId}/stops/{stopId}` | Remover parada da rota |

#### Transporte Público - Horários
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/admin/transit/schedules` | Criar perfil de horário |
| `GET` | `/admin/transit/schedules` | Listar horários |
| `GET` | `/admin/transit/schedules/{id}` | Buscar horário |
| `PUT` | `/admin/transit/schedules/{id}` | Atualizar horário |
| `DELETE` | `/admin/transit/schedules/{id}` | Deletar horário |
| `POST` | `/admin/transit/schedules/{id}/departures` | Adicionar horário de partida |
| `DELETE` | `/admin/transit/schedules/departures/{id}` | Remover horário de partida |
| `GET` | `/admin/transit/schedules/by-route/{routeId}` | Listar horários por rota |

#### Transporte Público - Viagens
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/admin/transit/trips` | Criar/Alocar viagem |
| `GET` | `/admin/transit/trips` | Listar viagens |
| `GET` | `/admin/transit/trips/{id}` | Buscar viagem |
| `PUT` | `/admin/transit/trips/{id}` | Atualizar viagem |
| `DELETE` | `/admin/transit/trips/{id}` | Deletar viagem |
| `POST` | `/admin/transit/trips/{id}/cancel` | Cancelar viagem |

---

## ⚙️ Configuração e Instalação

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.8+**
- **PostgreSQL 12+** (ou H2 para testes)
- **IDE**: IntelliJ IDEA, Eclipse ou VS Code

### Configuração do Banco de Dados

#### Opção 1: H2 (Desenvolvimento)

O projeto já vem configurado com H2. As configurações estão em `application.properties`:

```properties
# H2 Database
spring.datasource.url=jdbc:h2:file:~/sma1/h2db;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Acesse o console H2 em: `http://localhost:8084/h2-console`

#### Opção 2: PostgreSQL (Produção)

1. Crie um banco de dados:
```sql
CREATE DATABASE sma_db;
CREATE USER sma_user WITH PASSWORD 'sua_senha';
GRANT ALL PRIVILEGES ON DATABASE sma_db TO sma_user;
```

2. Atualize o `application.properties`:
```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/sma_db
spring.datasource.username=sma_user
spring.datasource.password=sua_senha
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### Configuração JWT

Configure a chave secreta do JWT (gere uma chave Base64 forte):

```properties
jwt.secret-key=SUA_CHAVE_SECRETA_BASE64_AQUI
jwt.expiration-time-ms=3600000
jwt.refresh-token.expiration-time-ms=86400000
```

### Instalação

1. **Clone o repositório**:
```bash
git clone https://github.com/seu-usuario/monitoramento.git
cd monitoramento
```

2. **Compile o projeto**:
```bash
mvn clean install
```

3. **Execute a aplicação**:
```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8084`

### Data Seeder

Na primeira execução, o sistema populará automaticamente o banco com dados de teste através do `DataSeeder.java`:

**Dados criados automaticamente:**
- 5 Roles
- 3 Departamentos e 3 Frotas
- 7 Usuários (admin, managers, drivers, passenger, system)
- 4 Dispositivos GPS
- 3 Ativos (veículos)
- 1 Rota com 4 Paradas
- 10 Horários de partida
- 2 Viagens alocadas para hoje

---

## 🚀 Uso

### Swagger UI

Acesse a documentação interativa da API em:
```
http://localhost:8084/swagger-ui.html
```

### Exemplo de Requisição - Login

```bash
curl -X POST http://localhost:8084/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "admin",
    "password": "admin_pass"
  }'
```

**Resposta:**
```json
{
  "status": 200,
  "message": "Login realizado com sucesso",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
    "expiresIn": 3600000
  }
}
```

### Exemplo de Requisição - Ingestão GPS (Dispositivo)

```bash
curl -X POST http://localhost:8084/api/v1/tracking/ingest \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DO_SYSTEM" \
  -d '{
    "deviceSerial": "SERIAL-BUS-001",
    "latitude": -15.7997,
    "longitude": -47.8931,
    "speed": 45.5,
    "heading": 180.0
  }'
```

**Resposta:**
```json
{
  "status": 202,
  "message": "Dados recebidos e estão sendo processados",
  "data": null
}
```

### Exemplo de Requisição - Listar Ativos Públicos

```bash
curl -X GET http://localhost:8084/api/v1/public/assets/live
```

**Resposta:**
```json
{
  "status": 200,
  "message": "Ativos públicos recuperados com sucesso",
  "data": [
    {
      "assetId": 2,
      "assetName": "Ônibus 101 - Centro",
      "latitude": -15.7997,
      "longitude": -47.8931,
      "speed": 45.5,
      "heading": 180.0,
      "lastUpdate": "2025-11-07T10:30:00"
    }
  ]
}
```

---

## 🧪 Fluxo de Teste

### Teste End-to-End Completo

Use Postman, Insomnia ou cURL para seguir este fluxo:

#### 1. Login como Admin
```bash
POST /api/v1/auth/login
Body: {"login": "admin", "password": "admin_pass"}
```
→ Copie o `accessToken`

#### 2. Listar Usuários (como Admin)
```bash
GET /api/v1/admin/users
Header: Authorization: Bearer {accessToken}
```

#### 3. Login como Sistema (Dispositivo)
```bash
POST /api/v1/auth/login
Body: {"login": "system_device", "password": "system_pass"}
```

#### 4. Enviar Dados GPS
```bash
POST /api/v1/tracking/ingest
Header: Authorization: Bearer {systemToken}
Body: {
  "deviceSerial": "SERIAL-BUS-001",
  "latitude": -15.8010,
  "longitude": -47.8950,
  "speed": 50.0,
  "heading": 90.0
}
```

#### 5. Visualizar Ativos Públicos (Sem autenticação)
```bash
GET /api/v1/public/assets/live
```
→ Você verá o ônibus com a localização atualizada

#### 6. Login como Gerente
```bash
POST /api/v1/auth/login
Body: {"login": "manager_transporte", "password": "manager_pass"}
```

#### 7. Ver Status da Frota (como Gerente)
```bash
GET /api/v1/manager/fleet/status
Header: Authorization: Bearer {managerToken}
```
→ Você verá apenas os ativos do departamento de Transporte

#### 8. Login como Motorista
```bash
POST /api/v1/auth/login
Body: {"login": "motorista01", "password": "driver_pass"}
```

#### 9. Ver Minhas Viagens de Hoje
```bash
GET /api/v1/driver/trips/my-today
Header: Authorization: Bearer {driverToken}
```

#### 10. Iniciar Viagem
```bash
POST /api/v1/driver/trips/{tripId}/start
Header: Authorization: Bearer {driverToken}
```

#### 11. Finalizar Viagem
```bash
POST /api/v1/driver/trips/{tripId}/end
Header: Authorization: Bearer {driverToken}
```

#### 12. Login como Passageiro
```bash
POST /api/v1/auth/login
Body: {"login": "passageiro", "password": "pass_123"}
```

#### 13. Favoritar uma Rota
```bash
POST /api/v1/favorites/{routeId}
Header: Authorization: Bearer {passengerToken}
```

#### 14. Ver Minhas Rotas Favoritas
```bash
GET /api/v1/favorites/my-favorites
Header: Authorization: Bearer {passengerToken}
```

---

## 📊 Estrutura de Resposta Padrão

Todas as respostas da API seguem o padrão `ApiResponseDTO`:

### Sucesso
```json
{
  "status": 200,
  "message": "Operação realizada com sucesso",
  "data": { ... }
}
```

### Erro de Validação
```json
{
  "status": 400,
  "message": "Erro de validação",
  "errors": [
    "O campo 'email' é obrigatório",
    "O campo 'senha' deve ter no mínimo 6 caracteres"
  ]
}
```

### Erro de Autorização
```json
{
  "status": 403,
  "message": "Você não tem permissão para acessar este recurso"
}
```

### Erro de Negócio
```json
{
  "status": 409,
  "message": "Dispositivo já está vinculado a outro ativo"
}
```

---

## 🔧 Configurações Adicionais

### Logs

Configure o nível de log em `application.properties`:

```properties
logging.level.root=INFO
logging.level.com.monitoramento=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### Pool de Conexões (HikariCP)

```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### Monitoramento (Actuator)

O projeto inclui Spring Boot Actuator para monitoramento:

```properties
management.endpoints.web.exposure.include=health,info,prometheus,metrics
management.endpoint.health.show-details=always
```

Endpoints disponíveis:
- Health: `http://localhost:8084/actuator/health`
- Metrics: `http://localhost:8084/actuator/metrics`

---

## 📝 Credenciais de Teste

Após executar o DataSeeder, as seguintes credenciais estarão disponíveis:

| Usuário | Login | Senha | Role |
|---------|-------|-------|------|
| Admin | `admin` | `admin_pass` | ADMIN |
| Gerente Transporte | `manager_transporte` | `manager_pass` | MANAGER |
| Gerente Saúde | `manager_saude` | `manager_pass` | MANAGER |
| Motorista 1 | `motorista01` | `driver_pass` | DRIVER |
| Motorista 2 | `motorista02` | `driver_pass` | DRIVER |
| Passageiro | `passageiro` | `pass_123` | PASSENGER |
| Sistema | `system_device` | `system_pass` | SYSTEM |

---

## 🤝 Contribuição

Contribuições são bem-vindas! Siga estas etapas:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

### Diretrizes de Código

- Siga os padrões de Clean Code
- Adicione testes para novas funcionalidades
- Mantenha a cobertura de testes acima de 80%
- Documente métodos públicos com JavaDoc
- Use commits semânticos (conventional commits)

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👥 Autores

- **SMA Team** - *Desenvolvimento inicial*

---

## 📞 Suporte

Para suporte, envie um email para suporte@sma.com ou abra uma issue no GitHub.

---

## 🗺️ Roadmap

- [ ] Implementar WebSocket para atualizações em tempo real
- [ ] Adicionar notificações por email
- [ ] Implementar sistema de alertas (atrasos, desvios de rota)
- [ ] Adicionar análise de dados e dashboards
- [ ] Implementar geofencing (cercas virtuais)
- [ ] Adicionar exportação de relatórios em PDF/CSV
- [ ] Implementar sistema de manutenção de veículos
- [ ] Adicionar integração com APIs de mapas (Google Maps, OpenStreetMap)
- [ ] Implementar aplicativo móvel (React Native)
- [ ] Adicionar suporte a múltiplos idiomas (i18n)

---

## 📚 Recursos Adicionais

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Documentação Spring Security](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/)
- [MapStruct Documentation](https://mapstruct.org/)
- [Swagger/OpenAPI](https://swagger.io/)

---

<div align="center">

**Desenvolvido com ❤️ para melhorar o transporte público**

⭐️ Se este projeto foi útil, considere dar uma estrela!

</div>
