package com.monitoramento.organization.infrastructure.persistence;

import com.monitoramento.organization.domain.model.Fleet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FleetRepository extends JpaRepository<Fleet, UUID> {

    Optional<Fleet> findByNameAndDepartmentId(String name, UUID departmentId);

    Optional<Fleet> findByNameAndDepartmentIdAndIdNot(String name, UUID departmentId, UUID id);

    Page<Fleet> findByDepartmentId(UUID departmentId, Pageable pageable);

    boolean existsByDepartmentId(UUID departmentId);
}