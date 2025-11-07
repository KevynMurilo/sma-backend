package com.monitoramento.shared.audit.infrastructure.persistence;

import com.monitoramento.shared.audit.domain.model.AuditLog;
import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByUserId(UUID userId, Pageable pageable);

    Page<AuditLog> findByAction(AuditAction action, Pageable pageable);

    Page<AuditLog> findByEntityType(AuditEntity entityType, Pageable pageable);

    Page<AuditLog> findByEntityTypeAndEntityId(AuditEntity entityType, String entityId, Pageable pageable);

    Page<AuditLog> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end, Pageable pageable);

    Page<AuditLog> findByUserIdAndTimestampBetween(UUID userId, OffsetDateTime start, OffsetDateTime end, Pageable pageable);
}
