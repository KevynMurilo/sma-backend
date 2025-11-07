package com.monitoramento.organization.infrastructure.persistence;

import com.monitoramento.organization.domain.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    boolean existsByNameOrCode(String name, String code);

    Optional<Department> findByNameAndIdNot(String name, UUID id);
    Optional<Department> findByCodeAndIdNot(String code, UUID id);
}