package com.monitoramento.organization.domain.useCase.department;

import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteDepartmentUseCase {

    private final DepartmentRepository departmentRepository;

    public ApiResponseDTO<Void> execute(UUID id) {
        return departmentRepository.findById(id)
                .map(department -> {
                    departmentRepository.delete(department);
                    return ApiResponseDTO.<Void>success(200, "Departamento deletado com sucesso");
                })
                .orElseGet(() -> ApiResponseDTO.empty(404, "Departamento não encontrado"));
    }
}
