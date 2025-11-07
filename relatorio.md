# Relatório de Implementação

## Configurações Gerais
- Configuração do Spring Security
- Implementação de JWT Authentication Filter
- Implementação de JWT Service
- Global Exception Handler
- Data Seeder para dados iniciais

## Módulo de Organização
- CRUD de Department
- CRUD de Fleet
- Relacionamento entre Department e Fleet

## Módulo de Assets
- CRUD de Monitored Asset
- CRUD de Tracking Device
- Sistema de vinculação de Device a Asset
- Sistema de desvinculação de Device de Asset
- Listagem de dispositivos não atribuídos
- Detalhes de veículo por Asset

## Módulo de Usuários e Autenticação
- CRUD de User
- Sistema de autenticação (login)
- Sistema de roles e permissões
- Update de permissões de usuário
- Sistema de rotas favoritas do usuário

## Módulo de Trânsito
- CRUD de Driver
- CRUD de Route
- CRUD de Stop
- CRUD de Schedule
- CRUD de Trip
- Sistema de atribuição de stops a rotas
- Sistema de departuras de schedules
- Sistema de início e fim de viagem pelo motorista
- Listagem de viagens do motorista

## Módulo de Tracking
- Controller de ingestão de dados de localização
- Processamento de eventos de tracking
- Repositório de status atual dos assets
- Repositório de pontos de localização históricos

## Endpoints Públicos
- Consulta de assets ativos ao vivo
- Listagem pública de rotas
- Detalhes públicos de rota por ID
- Consulta de schedules de uma rota

## Endpoints de Manager
- Status da frota do manager
- Criação de asset pelo manager

## DTOs e Mappers
- DTOs de request e response para todos os módulos
- Mappers para conversão entre entidades e DTOs
- DTOs simplificados para relacionamentos

## Melhorias no Modelo de Dados
- Ajustes em VehicleDetails
- Atualização de repositórios com queries customizadas
