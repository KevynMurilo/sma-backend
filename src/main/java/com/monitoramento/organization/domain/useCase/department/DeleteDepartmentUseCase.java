package com.monitoramento.organization.domain.useCase.department;

import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.organization.infrastructure.persistence.FleetRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteDepartmentUseCase {

    private final DepartmentRepository departmentRepository;
    private final FleetRepository fleetRepository;

    @Transactional
    public ApiResponseDTO<Void> execute(UUID id) {
        if (!departmentRepository.existsById(id)) {
            return ApiResponseDTO.empty(404, "Departamento não encontrado");
        }

        if (fleetRepository.existsByDepartmentId(id)) {
            return ApiResponseDTO.error(409, "Não é possível excluir o departamento pois ele possui frotas associadas.");
        }

        departmentRepository.deleteById(id);
        return ApiResponseDTO.success(200, "Departamento deletado com sucesso");
    }
}