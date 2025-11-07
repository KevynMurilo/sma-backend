package com.monitoramento.organization.domain.useCase.department;

import com.monitoramento.organization.api.dto.DepartmentRequestDTO;
import com.monitoramento.organization.api.dto.DepartmentResponseDTO;
import com.monitoramento.organization.api.mapper.DepartmentMapper;
import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateDepartmentUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public ApiResponseDTO<DepartmentResponseDTO> execute(UUID id, DepartmentRequestDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElse(null);

        if (department == null) {
            return ApiResponseDTO.empty(404, "Departamento não encontrado");
        }

        if (departmentRepository.findByNameAndIdNot(dto.name(), id).isPresent() ||
                departmentRepository.findByCodeAndIdNot(dto.code(), id).isPresent()) {
            return ApiResponseDTO.error(409, "O nome ou código informado já está em uso por outro departamento.");
        }

        department.setName(dto.name());
        department.setCode(dto.code());

        Department saved = departmentRepository.save(department);
        DepartmentResponseDTO responseDTO = departmentMapper.departmentToResponseDTO(saved);

        return ApiResponseDTO.success(200, "Departamento atualizado com sucesso", responseDTO);
    }
}