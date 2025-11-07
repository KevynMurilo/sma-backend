package com.monitoramento.shared.config;

import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.asset.domain.model.TrackingDevice;
import com.monitoramento.asset.domain.model.VehicleDetails;
import com.monitoramento.asset.domain.model.enums.AssetType;
import com.monitoramento.asset.domain.model.enums.DeviceStatus;
import com.monitoramento.asset.infrastructure.persistence.MonitoredAssetRepository;
import com.monitoramento.asset.infrastructure.persistence.TrackingDeviceRepository;
import com.monitoramento.asset.infrastructure.persistence.VehicleDetailsRepository;
import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.domain.model.Fleet;
import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.organization.infrastructure.persistence.FleetRepository;
import com.monitoramento.transit.domain.model.*;
import com.monitoramento.transit.domain.model.enums.DayOfWeekProfile;
import com.monitoramento.transit.domain.model.enums.RouteType;
import com.monitoramento.transit.domain.model.enums.TripStatus;
import com.monitoramento.transit.infrastructure.persistence.*;
import com.monitoramento.user.domain.model.Role;
import com.monitoramento.user.domain.model.User;
import com.monitoramento.user.infrastructure.persistence.RoleRepository;
import com.monitoramento.user.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final FleetRepository fleetRepository;
    private final MonitoredAssetRepository monitoredAssetRepository;
    private final TrackingDeviceRepository trackingDeviceRepository;
    private final VehicleDetailsRepository vehicleDetailsRepository;
    private final StopRepository stopRepository;
    private final RouteRepository routeRepository;
    private final RouteStopAssignmentRepository routeStopAssignmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDepartureRepository scheduleDepartureRepository;
    private final TripRepository tripRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Verifica se o banco já foi populado anteriormente
        if (roleRepository.count() > 0) {
            log.info("✓ O banco de dados já está populado. Seeder não será executado.");
            return;
        }

        log.info("========================================");
        log.info("  Iniciando Data Seeder - SMA Backend  ");
        log.info("========================================");

        // ============================================
        // FASE 1: ROLES (Papéis de Segurança)
        // ============================================
        log.info("→ Fase 1/7: Criando Roles (Papéis de Segurança)...");
        Role adminRole = createRole("ROLE_ADMIN");
        Role managerRole = createRole("ROLE_MANAGER");
        Role driverRole = createRole("ROLE_DRIVER");
        Role passengerRole = createRole("ROLE_PASSENGER");
        Role systemRole = createRole("ROLE_SYSTEM");
        log.info("  ✓ 5 Roles criadas com sucesso");

        // ============================================
        // FASE 2: ORGANIZAÇÃO (Departamentos e Frotas)
        // ============================================
        log.info("→ Fase 2/7: Criando Estrutura Organizacional...");
        Department saude = createDepartment("Secretaria de Saúde", "SES");
        Department transporte = createDepartment("Secretaria de Transporte Público", "STP");
        Department educacao = createDepartment("Secretaria de Educação", "SEMED");

        Fleet ambulancias = createFleet("Ambulâncias SAMU", saude);
        Fleet onibus = createFleet("Ônibus Circulares", transporte);
        Fleet transporteEscolar = createFleet("Transporte Escolar", educacao);
        log.info("  ✓ 3 Departamentos e 3 Frotas criadas");

        // ============================================
        // FASE 3: USUÁRIOS
        // ============================================
        log.info("→ Fase 3/7: Criando Usuários do Sistema...");

        // Admin - Acesso total
        createUser("admin", "admin_pass", "99999999999", "Admin Master", "admin@prefeitura.gov",
                Set.of(adminRole), null);

        // Passageiro - Usuário público
        createUser("passageiro", "pass_123", "11111111111", "Cidadão Teste", "cidadao@email.com",
                Set.of(passengerRole), null);

        // Motoristas
        User driver1 = createUser("motorista01", "driver_pass", "22222222222", "João Motorista",
                "joao.driver@prefeitura.gov", Set.of(driverRole), null);
        User driver2 = createUser("motorista02", "driver_pass", "55555555555", "Maria Condutora",
                "maria.driver@prefeitura.gov", Set.of(driverRole), null);

        // Gerentes - Acesso restrito aos seus departamentos
        createUser("manager_saude", "manager_pass", "33333333333", "Maria Gestora Saúde",
                "maria.gestora@prefeitura.gov", Set.of(managerRole), Set.of(saude));
        createUser("manager_transporte", "manager_pass", "44444444444", "Carlos Gestor Transporte",
                "carlos.gestor@prefeitura.gov", Set.of(managerRole), Set.of(transporte));

        // Sistema - Dispositivos IoT
        createUser("system_device", "system_pass", "00000000000", "Dispositivo de Ingestão",
                "system@prefeitura.gov", Set.of(systemRole), null);

        log.info("  ✓ 7 Usuários criados (1 Admin, 2 Managers, 2 Drivers, 1 Passenger, 1 System)");

        // ============================================
        // FASE 4: DISPOSITIVOS DE RASTREAMENTO
        // ============================================
        log.info("→ Fase 4/7: Criando Dispositivos de Rastreamento GPS...");
        TrackingDevice deviceAmbulancia = createDevice("SERIAL-AMB-001", "Arduino-GPS-A");
        TrackingDevice deviceOnibus = createDevice("SERIAL-BUS-001", "Arduino-GPS-B");
        TrackingDevice deviceEscolar = createDevice("SERIAL-ESC-001", "Arduino-GPS-D");
        createDevice("SERIAL-UNASSIGNED-001", "Arduino-GPS-C"); // Dispositivo não atribuído
        log.info("  ✓ 4 Dispositivos criados (3 serão vinculados, 1 disponível)");

        // ============================================
        // FASE 5: ATIVOS MONITORADOS (Veículos)
        // ============================================
        log.info("→ Fase 5/7: Criando Ativos Monitorados...");

        // Ativo Interno (Não Público) - Ambulância
        MonitoredAsset ambulancia = createAsset("Ambulância SAMU-01", AssetType.VAN,
                ambulancias, false, null, deviceAmbulancia);
        createVehicleDetails(ambulancia, "ABC1A23", "Mercedes Sprinter", "Mercedes", 2022);

        // Ativos Públicos - Ônibus
        MonitoredAsset onibus101 = createAsset("Ônibus 101 - Centro", AssetType.BUS,
                onibus, true, driver1, deviceOnibus);
        createVehicleDetails(onibus101, "XYZ9B87", "Torino", "Marcopolo", 2023);

        MonitoredAsset escolinha = createAsset("Van Escolar - Jardim Primavera", AssetType.VAN,
                transporteEscolar, true, driver2, deviceEscolar);
        createVehicleDetails(escolinha, "DEF5C67", "Ducato Escolar", "Fiat", 2021);

        log.info("  ✓ 3 Ativos criados (1 privado, 2 públicos)");

        // ============================================
        // FASE 6: TRANSPORTE PÚBLICO (Rotas e Horários)
        // ============================================
        log.info("→ Fase 6/7: Configurando Sistema de Transporte Público...");

        // Criação de Paradas (Stops)
        Stop stop1 = createStop("Terminal Central", "Plataforma A", -15.7997, -47.8931);
        Stop stop2 = createStop("Praça da Matriz", "Ponto em frente à igreja", -15.8010, -47.8950);
        Stop stop3 = createStop("Hospital Regional", "Entrada principal", -15.8025, -47.8988);
        Stop stop4 = createStop("Shopping Popular", "Ponto B - Estacionamento", -15.8040, -47.9000);

        // Criação de Rota
        Route route1 = createRoute("Linha 501 - Circular Centro",
                "Rota que passa pelo centro, hospital e shopping", RouteType.CIRCULAR);

        // Atribuição de Paradas à Rota (com ordem)
        createRouteAssignment(route1, stop1, 1);
        createRouteAssignment(route1, stop2, 2);
        createRouteAssignment(route1, stop3, 3);
        createRouteAssignment(route1, stop4, 4);

        // Criação de Horários (Schedule)
        Schedule scheduleWeekday = createSchedule(route1, DayOfWeekProfile.WEEKDAY);
        Schedule scheduleWeekend = createSchedule(route1, DayOfWeekProfile.SATURDAY);

        // Horários de Partida (Dias Úteis)
        ScheduleDeparture departure6h = createDeparture(scheduleWeekday, LocalTime.of(6, 0));
        ScheduleDeparture departure8h = createDeparture(scheduleWeekday, LocalTime.of(8, 0));
        createDeparture(scheduleWeekday, LocalTime.of(8, 30));
        createDeparture(scheduleWeekday, LocalTime.of(9, 0));
        createDeparture(scheduleWeekday, LocalTime.of(12, 0));
        createDeparture(scheduleWeekday, LocalTime.of(17, 30));
        createDeparture(scheduleWeekday, LocalTime.of(18, 0));

        // Horários de Partida (Fim de Semana)
        createDeparture(scheduleWeekend, LocalTime.of(8, 0));
        createDeparture(scheduleWeekend, LocalTime.of(12, 0));
        createDeparture(scheduleWeekend, LocalTime.of(17, 0));

        log.info("  ✓ 1 Rota com 4 Paradas, 2 Perfis de Horário e 10 Partidas configuradas");

        // ============================================
        // FASE 7: ALOCAÇÃO DE VIAGENS (Trips)
        // ============================================
        log.info("→ Fase 7/7: Alocando Viagens para Hoje...");

        // Aloca viagens para HOJE
        createTrip(departure6h, LocalDate.now(), escolinha, driver2); // Viagem de manhã cedo
        createTrip(departure8h, LocalDate.now(), onibus101, driver1); // Viagem das 8h

        log.info("  ✓ 2 Viagens alocadas para hoje");

        log.info("========================================");
        log.info("  ✅ Data Seeder finalizado com sucesso!");
        log.info("========================================");
        log.info("");
        log.info("🔑 Credenciais de Teste:");
        log.info("  Admin:    admin / admin_pass");
        log.info("  Manager:  manager_transporte / manager_pass");
        log.info("  Driver:   motorista01 / driver_pass");
        log.info("  Passenger: passageiro / pass_123");
        log.info("  System:   system_device / system_pass");
        log.info("========================================");
    }

    private Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }

    private User createUser(String username, String password, String cpf, String fullName, String email, Set<Role> roles, Set<Department> manageableDepartments) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setCpf(cpf);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setRoles(roles);
        if (manageableDepartments != null) {
            user.setManageableDepartments(manageableDepartments);
        }
        user.setEnabled(true);
        return userRepository.save(user);
    }

    private Department createDepartment(String name, String code) {
        Department dept = new Department();
        dept.setName(name);
        dept.setCode(code);
        return departmentRepository.save(dept);
    }

    private Fleet createFleet(String name, Department department) {
        Fleet fleet = new Fleet();
        fleet.setName(name);
        fleet.setDepartment(department);
        return fleetRepository.save(fleet);
    }

    private TrackingDevice createDevice(String serial, String model) {
        TrackingDevice device = new TrackingDevice();
        device.setDeviceSerial(serial);
        device.setModel(model);
        device.setStatus(DeviceStatus.UNASSIGNED);
        return trackingDeviceRepository.save(device);
    }

    private MonitoredAsset createAsset(String name, AssetType type, Fleet fleet, boolean isPublic, User driver, TrackingDevice device) {
        MonitoredAsset asset = new MonitoredAsset();
        asset.setName(name);
        asset.setType(type);
        asset.setFleet(fleet);
        asset.setPubliclyVisible(isPublic);

        if (driver != null) {
            asset.setCurrentDriver(driver);
        }

        if (device != null) {
            asset.setTrackingDevice(device);
            MonitoredAsset savedAsset = monitoredAssetRepository.save(asset); // Salva para obter o ID

            // Atualiza o dispositivo (simulando a lógica do UseCase)
            device.setAssignedAsset(savedAsset);
            device.setStatus(DeviceStatus.OFFLINE);
            trackingDeviceRepository.save(device);

            return savedAsset;
        }

        return monitoredAssetRepository.save(asset);
    }

    private void createVehicleDetails(MonitoredAsset asset, String plate, String model, String make, int year) {
        VehicleDetails details = new VehicleDetails();
        details.setAsset(asset); // <-- Correção do @MapsId (não definir o ID manualmente)
        details.setLicensePlate(plate);
        details.setModel(model);
        details.setMake(make);
        details.setYear(year);
        vehicleDetailsRepository.save(details);
    }

    private Stop createStop(String name, String description, double lat, double lon) {
        Stop stop = new Stop();
        stop.setName(name);
        stop.setDescription(description);
        stop.setLatitude(lat);
        stop.setLongitude(lon);
        return stopRepository.save(stop);
    }

    private Route createRoute(String name, String description, RouteType type) {
        Route route = new Route();
        route.setRouteName(name);
        route.setRouteDescription(description);
        route.setType(type);
        return routeRepository.save(route);
    }

    private void createRouteAssignment(Route route, Stop stop, int order) {
        RouteStopAssignment assignment = new RouteStopAssignment();
        assignment.setRoute(route);
        assignment.setStop(stop);
        assignment.setStopOrder(order);
        routeStopAssignmentRepository.save(assignment);
    }

    private Schedule createSchedule(Route route, DayOfWeekProfile profile) {
        Schedule schedule = new Schedule();
        schedule.setRoute(route);
        schedule.setDayProfile(profile);
        return scheduleRepository.save(schedule);
    }

    // Retorna a entidade para que possa ser usada na criação da Trip
    private ScheduleDeparture createDeparture(Schedule schedule, LocalTime time) {
        ScheduleDeparture departure = new ScheduleDeparture();
        departure.setSchedule(schedule);
        departure.setDepartureTime(time);
        return scheduleDepartureRepository.save(departure);
    }

    // Método createTrip adicionado
    private Trip createTrip(ScheduleDeparture departure, LocalDate date, MonitoredAsset asset, User driver) {
        Trip trip = new Trip();
        trip.setScheduleDeparture(departure);
        trip.setTripDate(date);
        trip.setAsset(asset);
        trip.setDriver(driver);
        trip.setStatus(TripStatus.SCHEDULED);
        return tripRepository.save(trip);
    }
}