# Projeto SMA: Sistema de Monitoramento de Ativos

**Versão do Documento:** 1.0.0
**Última Atualização:** 05 de Novembro de 2025

## 1. Visão Geral

O **SMA (Sistema de Monitoramento de Ativos)** é a plataforma de backend para o sistema de rastreamento de frotas da Prefeitura. Seu objetivo é gerenciar e monitorar dois tipos distintos de frotas com diferentes regras de visibilidade:

1.  **Frotas Públicas (Transporte):** Veículos como ônibus e vans, cujas localizações, rotas e horários devem ser visíveis para a população (cidadãos).
2.  **Frotas Internas (Governo):** Veículos de uso departamental (ex: carros da Secretaria de Saúde, caminhões da Secretaria de Obras), cujas localizações são confidenciais e restritas apenas a gestores autorizados.

Este documento serve como a principal fonte de verdade técnica para o desenvolvimento, detalhando a arquitetura, as regras de negócio e os fluxos de dados.

## 2. Funcionalidades Principais

* **Rastreamento em Tempo Real:** Ingestão de dados de GPS (via dispositivos como Arduino) e exibição da última localização conhecida.
* **Controle de Frota Hierárquico:** Gerenciamento de veículos por Departamentos (Secretarias) e Frotas (ex: "Ambulâncias").
* **Controle de Acesso por Papel (RBAC):** Sistema de permissões granular que restringe a visibilidade de dados com base no papel do usuário e em sua alocação departamental.
* **Gestão de Trânsito Público:** Capacidade de definir Rotas, Paradas e Horários para veículos de transporte público.
* **Cálculo de ETA (Estimativa de Chegada):** Lógica para processar a localização de veículos públicos e estimar o tempo de chegada nas próximas paradas.
* **Consultas Geoespaciais:** Uso de PostGIS para consultas de proximidade e distância.

---

## 3. Arquitetura da Aplicação (Package-by-Domain)

O sistema é modularizado em domínios de negócio para garantir baixo acoplamento e alta coesão.

* `/organization`
    * **Responsabilidade:** Define a hierarquia organizacional da prefeitura.
    * **Entidades Chave:** `Department` (Secretaria), `Fleet` (Frota).

* `/user`
    * **Responsabilidade:** Identidade, autenticação e permissões. Define *quem* é o usuário e *o que* ele pode ver/fazer.
    * **Entidades Chave:** `User`, `Role`, `User.manageableDepartments`.

* `/asset`
    * **Responsabilidade:** O catálogo de *coisas* que são rastreadas.
    * **Entidades Chave:** `MonitoredAsset` (o veículo), `TrackingDevice` (o Arduino), `VehicleDetails`.

* `/tracking`
    * **Responsabilidade:** A ingestão e armazenamento de dados de geolocalização. Responde "Onde o ativo está agora?" e "Onde ele esteve?".
    * **Entidades Chave:** `LocationDataPoint` (Histórico), `AssetCurrentStatus` (Snapshot).

* `/transit`
    * **Responsabilidade:** A lógica de *negócio* para transporte público.
    * **Entidades Chave:** `Route`, `Stop`, `Schedule`, `ScheduledTrip` (NOVA), `Trip` (Viagem).

* `/shared`
    * **Responsabilidade:** Configurações globais (Spring Security), tratamento de exceções, utilitários (ex: JWT) e beans compartilhados.

---

## 4. Glossário de Conceitos Centrais

* **Department (Secretaria):** A unidade organizacional de nível superior (ex: "Secretaria de Saúde").
* **Fleet (Frota):** Um subconjunto de veículos pertencente a um `Department` (ex: Frota "Ambulâncias" dentro da "Secretaria de Saúde").
* **MonitoredAsset (Ativo):** O veículo em si (um ônibus, um carro administrativo).
    * `isPubliclyVisible`: O *flag booleano* que define se este ativo é público (`true` = ônibus) ou interno (`false` = carro da secretaria).
* **TrackingDevice (Dispositivo):** O hardware físico (Arduino) que está instalado no `Asset` e envia os dados de GPS.
* **LocationDataPoint (Ponto de Localização):** Um registro no banco de dados para *cada* coordenada GPS recebida. Esta é a tabela de histórico (Big Data).
* **AssetCurrentStatus (Status Atual):** Uma tabela de "snapshot" com apenas *uma linha por ativo*. Ela armazena a *última localização conhecida* para consultas rápidas.
* **Route, Stop, Schedule (Rota, Parada, Horário):** As definições de planejamento do transporte público.
* **ScheduledTrip (Viagem Agendada):** O "Template" da viagem. Ex: (Linha 501, Partida 08:00, Dias Úteis).
* **Trip (Viagem):** A *instância real* de uma `ScheduledTrip` em um dia específico. Ex: (A viagem das 08:00 de **hoje**, 05/11, com o Ônibus X e Motorista Y).

---

## 5. Regras de Negócio por Ator (Papel)

Esta seção detalha as permissões e fluxos exatos para cada papel no sistema.

### 5.1. `ROLE_SYSTEM` (O Dispositivo / Arduino)

Este é um ator "máquina", o seu dispositivo de hardware.

* **O que faz? (Ações):**
    * **Ingestão de Dados:** Deve enviar um *heartbeat* de localização para `POST /api/v1/tracking/ingest`.
    * **Frequência de Ingestão:** Recomenda-se **a cada 30 segundos** (em movimento) e **a cada 5 minutos** (parado).
    * **Autenticação:** Token JWT de longa duração com `ROLE_SYSTEM`.

* **O que vê? (Visibilidade):** Nenhuma (write-only).

* **Processamento de Backend (Fluxo Assíncrono):**
    1.  O Endpoint `.../ingest` **DEVE** ser assíncrono.
    2.  Ele apenas valida o DTO, publica a `IngestionRequest` em uma fila (RabbitMQ, Kafka) e retorna `HTTP 202 Accepted`.
    3.  Um *Consumer* (worker) escuta a fila e executa a lógica de negócio:
        a. Valida se o `deviceSerial` está vinculado a um `MonitoredAsset`.
        b. Salva o `LocationDataPoint` (histórico).
        c. Faz `UPSERT` no `AssetCurrentStatus` (snapshot).
        d. Define o status (`MOVING`, `STOPPED`).
        e. Dispara um evento (`LocationUpdatedEvent`) para o resto do sistema.

### 5.2. `ROLE_ADMIN` (O Administrador Master)

O superusuário do sistema.

* **O que faz? (Ações):** CRUD irrestrito em todas as entidades de configuração (`Department`, `Fleet`, `User`, `Asset`, `Device`, `Route`, `Stop`, `Schedule`, `ScheduledTrip`).
* **O que vê? (Visibilidade):** **VISIBILIDADE TOTAL.** Vê 100% da frota (interna e pública).

### 5.3. `ROLE_MANAGER` (O Gestor de Departamento/Frota)

O usuário de "Controle de Frotas Internas".

* **O que faz? (Ações):**
    * **CRUD Restrito:** Gerencia `Fleet`s e `MonitoredAsset`s **apenas** dentro dos seus `manageableDepartments`.
    * **Alocação:** Aloca `Asset`s e `Driver`s para `Trip`s com status `SCHEDULED`.
* **O que vê? (Visibilidade):**
    * **VISIBILIDADE RESTRITA.** Vê **APENAS** os `MonitoredAsset`s que pertencem aos `Department`s que ele gerencia.
    * Acesso ao `GET /api/v1/manager/fleet/status` (ver Seção 7).

### 5.4. `ROLE_DRIVER` (O Motorista)

O operador do veículo.

* **O que faz? (Ações):**
    * Vê suas `Trip`s alocadas para o dia.
    * Inicia uma viagem: `POST /api/v1/driver/trips/{tripId}/start`. (Muda `Trip.status` de `SCHEDULED` para `IN_PROGRESS`).
    * Finaliza uma viagem: `POST /api/v1/driver/trips/{tripId}/end`. (Muda `Trip.status` para `COMPLETED`).
* **O que vê? (Visibilidade):** **MÍNIMA.** Vê apenas a `Trip` e `Asset` aos quais está atualmente alocado.

### 5.5. `ROLE_PASSENGER` (O Cidadão / População)

O usuário final, público.

* **O que faz? (Ações):** Apenas consulta (Read-Only).
* **O que vê? (Visibilidade):** **PÚBLICA RESTRITA.**
    * Vê **APENAS** `MonitoredAsset`s que estão com `isPubliclyVisible = true` E em uma `Trip` com `status = IN_PROGRESS`.
    * Acesso ao `GET /api/v1/public/routes/{routeId}/status`.

### 5.6. Fluxo de Sistema (Automático)

* **Processamento de Trânsito (`@EventListener`):**
    * **Gatilho:** `LocationUpdatedEvent` (disparado pelo Fluxo 5.1).
    * **Ação:** Verifica se o `Asset` está em uma `Trip` `IN_PROGRESS`. Se sim, calcula a distância (PostGIS `ST_Distance`) até a `nextStop`. Se `distancia < 50m` (limiar), atualiza a `Trip` para a próxima parada. Se for a última parada, marca `Trip` como `COMPLETED`.

* **Geração de Viagens (`@Scheduled`):**
    * **Gatilho:** Todo dia à 00:01 (configurável).
    * **Ação:** O `TripCreationService` busca todas as `ScheduledTrip`s que devem rodar no dia de hoje (ex: "Dias Úteis" e hoje é quarta-feira).
    * Para cada uma, ele cria uma nova entidade `Trip` com `status = SCHEDULED` e `date = [hoje]`, pronta para ser alocada por um `ROLE_MANAGER`.

---

## 6. Tecnologias Utilizadas

* **Core:** Java (17+), Spring Boot 3
* **Dados:** Spring Data JPA
* **Banco de Dados:** PostgreSQL
* **Geoespacial:** PostGIS (Extensão do Postgres) + `org.hibernate.orm:hibernate-spatial`
* **Geometria:** `org.locationtech.jts:jts-core`
* **Segurança:** Spring Security (com Autenticação JWT)
* **Mensageria (Recomendado):** RabbitMQ ou Kafka (para ingestão assíncrona)
* **Utilidades:** Lombok

---

## 7. Catálogo da API (Contratos e DTOs Principais)

Esta é a definição dos contratos de API para os fluxos essenciais.

### 7.1. Autenticação

`POST /api/v1/auth/login`
* **Permissão:** Pública
* **Request:** `AuthRequest { String cpf, String password }`
* **Response:** `AuthResponse { String token, UserDetailsDTO user }`

`POST /api/v1/auth/simulate`
* **Permissão:** Pública (Ativo apenas em perfil `dev`)
* **Request:** `SimulateRequest { String cpf }`
* **Response:** `AuthResponse { String token, UserDetailsDTO user }`

### 7.2. Ingestão de Dados (Tracking)

`POST /api/v1/tracking/ingest`
* **Permissão:** `ROLE_SYSTEM`
* **Request:** `IngestionRequest { String deviceSerial, double latitude, double longitude, double speed, OffsetDateTime timestamp }`
* **Response:** `HTTP 202 Accepted` (Payload enviado para a fila)

### 7.3. Consulta de Frota (Gerente Interno)

`GET /api/v1/manager/fleet/status`
* **Permissão:** `ROLE_MANAGER`
* **Regra:** O backend **automaticamente** filtra os resultados para mostrar apenas ativos dos `manageableDepartments` do usuário logado.
* **Response:** `List<AssetStatusResponse>`
* **DTO:** `AssetStatusResponse { UUID assetId, String assetName, String licensePlate, AssetStatus status, PointDTO lastCoordinates, OffsetDateTime lastUpdated, DriverDTO currentDriver }`

### 7.4. Consulta Pública (Cidadão)

`GET /api/v1/public/routes/{routeId}/status`
* **Permissão:** `ROLE_PASSENGER` (ou Pública)
* **Regra:** O backend **automaticamente** filtra para mostrar apenas `Trip`s `IN_PROGRESS` de ativos com `isPubliclyVisible = true`.
* **Response:** `List<PublicVehicleStatus>`
* **DTO:** `PublicVehicleStatus { UUID assetId, String assetName, PointDTO lastCoordinates, String nextStopName, int etaMinutes }`

### 7.5. Operações do Motorista (Driver)

`POST /api/v1/driver/trips/{tripId}/start`
* **Permissão:** `ROLE_DRIVER`
* **Regra:** O usuário só pode iniciar `Trip`s que estão alocadas a ele e com status `SCHEDULED`.
* **Response:** `HTTP 200 OK` (com a `Trip` atualizada)

`POST /api/v1/driver/trips/{tripId}/end`
* **Permissão:** `ROLE_DRIVER`
* **Response:** `HTTP 200 OK` (com a `Trip` atualizada)

### 7.6. Administração (Exemplos)

`POST /api/v1/admin/departments`
`POST /api/v1/admin/fleets`
`POST /api/v1/admin/assets/{assetId}/assign-device`

---

## 8. Padrões de Resposta e Tratamento de Exceções

O sistema deve usar um `GlobalExceptionHandler` (`@ControllerAdvice`) para padronizar todas as respostas de erro.

* `HTTP 200 OK`: Sucesso em operações `GET`.
* `HTTP 201 Created`: Sucesso em operações `POST` (criação de recurso).
* `HTTP 202 Accepted`: Sucesso em operações assíncronas (ex: `.../ingest`).
* `HTTP 204 No Content`: Sucesso em operações `DELETE`.
* **`HTTP 400 Bad Request`**: Erro de validação de DTO. O corpo da resposta deve conter os campos que falharam.
* **`HTTP 401 Unauthorized`**: O Token JWT está ausente, expirado ou inválido.
* **`HTTP 403 Forbidden`**: O Token JWT é válido, mas o usuário (`Role`) não tem permissão para acessar o recurso (ex: `ROLE_PASSENGER` tentando acessar `.../admin/...`).
* **`HTTP 404 Not Found`**: O recurso solicitado não existe (ex: `GET .../assets/{uuid-invalido}`).
* **`HTTP 409 Conflict`**: Violação de regra de negócio (ex: tentar criar um `Department` com nome duplicado; tentar iniciar uma `Trip` já `COMPLETED`).
* **`HTTP 500 Internal Server Error`**: Erro inesperado no servidor (bug).

---

## 9. Diretrizes de Implementação: Performance e Banco de Dados

Estas não são sugestões, são **requisitos** para a escalabilidade do sistema.

1.  **Padrão SRID (Obrigatório):**
    * Todas as colunas de geometria (ex: `Point`, `Polygon`) **DEVEM** usar o SRID `4326 (WGS 84)`.
    * Configuração no `application.properties`: `spring.jpa.properties.hibernate.dialect=org.hibernate.spatial.dialect.postgresql.PostgisDialect`

2.  **Índices Geoespaciais (Obrigatório):**
    * Consultas de proximidade (ex: "ônibus perto da parada") são a base do `transit`.
    * A coluna `lastCoordinates` (em `AssetCurrentStatus`) **DEVE** ter um índice GIST:
        * `CREATE INDEX idx_asset_status_coords ON asset_current_status USING GIST(last_coordinates);`
    * A coluna `location` (em `Stop`) **DEVE** ter um índice GIST:
        * `CREATE INDEX idx_stop_location ON transit_stops USING GIST(location);`

3.  **Índice de Histórico (Obrigatório):**
    * A tabela `location_data_point` será a maior do banco. Para consultas de histórico ("Onde esteve o veículo X ontem?"), ela precisa de um índice B-tree composto:
        * `CREATE INDEX idx_location_history_device_time ON location_data_point (device_id, timestamp DESC);`

4.  **Particionamento de Tabela (Obrigatório):**
    * A tabela `location_data_point` se tornará lenta para escrita (INSERT) se crescer demais.
    * Ela **DEVE** ser particionada usando o particionamento nativo do PostgreSQL.
    * **Estratégia:** Particionamento por `RANGE` na coluna `timestamp`.
    * **Exemplo:** Criar uma nova partição (tabela filha) para cada mês (ex: `location_data_point_2025_11`, `location_data_point_2025_12`).