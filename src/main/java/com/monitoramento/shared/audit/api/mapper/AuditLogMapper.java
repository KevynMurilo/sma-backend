package com.monitoramento.shared.audit.api.mapper;

import com.monitoramento.shared.audit.api.dto.AuditLogResponseDTO;
import com.monitoramento.shared.audit.domain.model.AuditLog;
import org.mapstruct.Mapper;

@Mapper
public interface AuditLogMapper {
    AuditLogResponseDTO toResponseDTO(AuditLog auditLog);
}
