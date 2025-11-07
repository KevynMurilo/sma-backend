package com.monitoramento.shared.audit.domain.useCase;

import com.monitoramento.shared.audit.api.dto.AuditLogResponseDTO;
import com.monitoramento.shared.audit.api.mapper.AuditLogMapper;
import com.monitoramento.shared.audit.domain.model.AuditLog;
import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.infrastructure.persistence.AuditLogRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListAuditLogsUseCase {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public ApiResponseDTO<PagedResponseDTO<AuditLogResponseDTO>> execute(
            UUID userId,
            AuditAction action,
            AuditEntity entityType,
            String entityId,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            Pageable pageable) {

        try {
            Page<AuditLog> auditLogs;

            if (userId != null && startDate != null && endDate != null) {
                auditLogs = auditLogRepository.findByUserIdAndTimestampBetween(userId, startDate, endDate, pageable);
            } else if (startDate != null && endDate != null) {
                auditLogs = auditLogRepository.findByTimestampBetween(startDate, endDate, pageable);
            } else if (entityType != null && entityId != null) {
                auditLogs = auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable);
            } else if (entityType != null) {
                auditLogs = auditLogRepository.findByEntityType(entityType, pageable);
            } else if (action != null) {
                auditLogs = auditLogRepository.findByAction(action, pageable);
            } else if (userId != null) {
                auditLogs = auditLogRepository.findByUserId(userId, pageable);
            } else {
                auditLogs = auditLogRepository.findAll(pageable);
            }

            PagedResponseDTO<AuditLogResponseDTO> response = PagedResponseDTO.from(
                    auditLogs.map(auditLogMapper::toResponseDTO)
            );

            return ApiResponseDTO.success(200, "Audit logs retrieved successfully", response);
        } catch (Exception e) {
            log.error("Error listing audit logs", e);
            return ApiResponseDTO.error(500, "Error listing audit logs: " + e.getMessage());
        }
    }
}
