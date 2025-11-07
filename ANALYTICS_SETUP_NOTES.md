# Analytics System - Setup Notes

## Manual Updates Required

Due to file watching constraints, the following manual updates are needed:

### 1. Update AuditAction Enum
File: `src/main/java/com/monitoramento/shared/audit/domain/model/enums/AuditAction.java`

Add the following value at the end of the enum (before the closing brace):
```
    REPORT
```

Final enum should look like:
```java
public enum AuditAction {
    CREATE,
    UPDATE,
    DELETE,
    LOGIN,
    LOGOUT,
    LOGIN_FAILED,
    ACCESS_DENIED,
    ASSIGN,
    UNASSIGN,
    START,
    END,
    CANCEL,
    EXPORT,
    IMPORT,
    REPORT
}
```

### 2. Update AuditEntity Enum
File: `src/main/java/com/monitoramento/shared/audit/domain/model/enums/AuditEntity.java`

Add the following value at the end of the enum (before the closing brace):
```
    ANALYTICS
```

Final enum should look like:
```java
public enum AuditEntity {
    USER,
    DEPARTMENT,
    FLEET,
    ASSET,
    DEVICE,
    ROUTE,
    STOP,
    SCHEDULE,
    DEPARTURE,
    TRIP,
    FAVORITE_ROUTE,
    LOCATION,
    GEOFENCE,
    SYSTEM,
    ANALYTICS  // <-- Add this
}
```

### 3. Update Repository Methods

The following repositories need custom query methods added. Reference the `AnalyticsQueries.java` interface for the query strings.

#### AssetCurrentStatusRepository
File: `src/main/java/com/monitoramento/tracking/infrastructure/persistence/AssetCurrentStatusRepository.java`

Add before the closing brace:
```java
    // Analytics queries
    @Query("SELECT COUNT(acs) FROM AssetCurrentStatus acs JOIN acs.asset a WHERE a.fleet.id = :fleetId AND acs.status = :status")
    Long countByFleetIdAndStatus(@Param("fleetId") UUID fleetId, @Param("status") com.monitoramento.tracking.domain.model.enums.AssetStatus status);

    @Query("SELECT COUNT(acs) FROM AssetCurrentStatus acs JOIN acs.asset a WHERE a.fleet.id = :fleetId")
    Long countByFleetId(@Param("fleetId") UUID fleetId);

    @Query("SELECT COUNT(acs) FROM AssetCurrentStatus acs JOIN acs.asset a WHERE a.fleet.department.id = :departmentId")
    Long countByDepartmentId(@Param("departmentId") UUID departmentId);
```

#### TripRepository
File: `src/main/java/com/monitoramento/transit/infrastructure/persistence/TripRepository.java`

Add before the closing brace:
```java
    // Analytics queries
    @Query("SELECT COUNT(t) FROM Trip t WHERE t.asset.fleet.id = :fleetId AND t.tripDate = :date AND t.status = :status")
    Long countByFleetIdAndDateAndStatus(UUID fleetId, LocalDate date, TripStatus status);

    @Query("SELECT COUNT(t) FROM Trip t WHERE t.driver.id = :driverId AND t.tripDate BETWEEN :startDate AND :endDate")
    Long countByDriverIdAndDateRange(UUID driverId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(t) FROM Trip t WHERE t.driver.id = :driverId AND t.tripDate BETWEEN :startDate AND :endDate AND t.status = :status")
    Long countByDriverIdAndDateRangeAndStatus(UUID driverId, LocalDate startDate, LocalDate endDate, TripStatus status);

    @Query("SELECT t FROM Trip t WHERE t.driver.id = :driverId AND t.tripDate BETWEEN :startDate AND :endDate AND t.status = 'COMPLETED'")
    List<Trip> findCompletedTripsByDriverAndDateRange(UUID driverId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(t) FROM Trip t WHERE t.asset.id = :assetId AND t.tripDate BETWEEN :startDate AND :endDate")
    Long countByAssetIdAndDateRange(UUID assetId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(t) FROM Trip t WHERE t.asset.id = :assetId AND t.tripDate BETWEEN :startDate AND :endDate AND t.status = :status")
    Long countByAssetIdAndDateRangeAndStatus(UUID assetId, LocalDate startDate, LocalDate endDate, TripStatus status);

    @Query("SELECT t FROM Trip t WHERE t.asset.id = :assetId AND t.tripDate BETWEEN :startDate AND :endDate AND t.status = 'COMPLETED'")
    List<Trip> findCompletedTripsByAssetAndDateRange(UUID assetId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(t) FROM Trip t WHERE t.asset.fleet.department.id = :departmentId AND t.tripDate BETWEEN :startDate AND :endDate")
    Long countByDepartmentIdAndDateRange(UUID departmentId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(t) FROM Trip t WHERE t.asset.fleet.department.id = :departmentId AND t.tripDate BETWEEN :startDate AND :endDate AND t.status = :status")
    Long countByDepartmentIdAndDateRangeAndStatus(UUID departmentId, LocalDate startDate, LocalDate endDate, TripStatus status);

    @Query("SELECT t FROM Trip t WHERE t.asset.fleet.department.id = :departmentId AND t.tripDate BETWEEN :startDate AND :endDate AND t.status = 'COMPLETED'")
    List<Trip> findCompletedTripsByDepartmentAndDateRange(UUID departmentId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(DISTINCT t.driver.id) FROM Trip t WHERE t.asset.fleet.department.id = :departmentId")
    Long countDistinctDriversByDepartmentId(UUID departmentId);
```

#### FleetRepository
File: `src/main/java/com/monitoramento/organization/infrastructure/persistence/FleetRepository.java`

Add before the closing brace:
```java
    // Analytics queries
    Long countByDepartmentId(UUID departmentId);
```

## System Overview

The analytics system has been fully implemented with the following components:

### DTOs (Data Transfer Objects)
- `TripStatisticsDTO` - Estatísticas detalhadas de viagens
- `FleetDashboardDTO` - Dashboard agregado de frotas
- `DriverPerformanceDTO` - Performance de motoristas
- `AssetUtilizationDTO` - Utilização de ativos
- `DepartmentReportDTO` - Relatório consolidado de departamentos

### Mappers
- `TripStatisticsMapper`
- `FleetDashboardMapper`
- `DriverPerformanceMapper`
- `AssetUtilizationMapper`
- `DepartmentReportMapper`

### Use Cases
- `GetTripStatisticsUseCase` - Calcula estatísticas de viagem usando LocationDataPoints
- `GetFleetDashboardUseCase` - Gera dashboard de frota com estatísticas em tempo real
- `GetDriverPerformanceUseCase` - Analisa performance de motorista com filtro de período
- `GetAssetUtilizationUseCase` - Calcula utilização de ativo
- `GetDepartmentReportUseCase` - Gera relatório consolidado de departamento

### Services
- `AnalyticsCalculationService` - Serviço centralizado para cálculos de analytics

### Controller
- `AnalyticsController` - Endpoints REST em `/api/v1/admin/analytics`
  - GET `/trips/{tripId}/statistics` - Estatísticas de viagem
  - GET `/fleets/{fleetId}/dashboard` - Dashboard de frota
  - GET `/drivers/{driverId}/performance` - Performance de motorista (com startDate e endDate)
  - GET `/assets/{assetId}/utilization` - Utilização de ativo (com startDate e endDate)
  - GET `/departments/{departmentId}/report` - Relatório de departamento (com startDate e endDate, ADMIN only)

### Security
- Todos os endpoints requerem role ADMIN ou MANAGER
- Endpoint de relatório de departamento requer role ADMIN
- Todas as operações são auditadas com AuditService

### Calculation Logic
- **Distância Total**: Calculada usando GeoUtils.calculateHaversineDistance entre pontos consecutivos
- **Duração**: Diferença entre actualStartTime e actualEndTime
- **Velocidade Média**: distância total / duração (em km/h)
- **Tempo Parado**: Soma dos períodos com velocidade < 5 km/h
- **Tempo em Movimento**: Soma dos períodos com velocidade >= 5 km/h
- **Tempo Idle**: Soma dos períodos com velocidade < 1 km/h
