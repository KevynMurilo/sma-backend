package com.monitoramento.shared.audit.api.dto;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AuditLogResponseDTO(
        Long id,
        UUID userId,
        String username,
        AuditAction action,
        AuditEntity entityType,
        String entityId,
        String description,
        String ipAddress,
        String userAgent,
        OffsetDateTime timestamp,
        String oldValue,
        String newValue
) {}
