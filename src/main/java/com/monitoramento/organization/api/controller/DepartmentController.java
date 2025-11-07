package com.monitoramento.organization.api.controller;

import com.monitoramento.organization.api.dto.DepartmentRequestDTO;
import com.monitoramento.organization.api.dto.DepartmentResponseDTO;
import com.monitoramento.organization.domain.useCase.department.CreateDepartmentUseCase;
import com.monitoramento.organization.domain.useCase.department.DeleteDepartmentUseCase;
import com.monitoramento.organization.domain.useCase.department.GetAllDepartmentUseCase;
import com.monitoramento.organization.domain.useCase.department.UpdateDepartmentUseCase;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/departments") // <-- ROTA CORRIGIDA (Protegida)
@RequiredArgsConstructor
public class DepartmentController {

    private final CreateDepartmentUseCase createDepartmentUseCase;
    private final GetAllDepartmentUseCase getAllDepartmentUseCase;
    private final DeleteDepartmentUseCase deleteDepartmentUseCase;
    private final UpdateDepartmentUseCase updateDepartmentUseCase;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<DepartmentResponseDTO>>> getAll(Pageable pageable) {
        ApiResponseDTO<PagedResponseDTO<DepartmentResponseDTO>> response = getAllDepartmentUseCase.execute(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> createDepartment(
            @Valid @RequestBody DepartmentRequestDTO requestDTO) {
        ApiResponseDTO<DepartmentResponseDTO> response = createDepartmentUseCase.execute(requestDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentRequestDTO requestDTO) {
        ApiResponseDTO<DepartmentResponseDTO> response = updateDepartmentUseCase.execute(id, requestDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteDepartment(@PathVariable UUID id) {
        ApiResponseDTO<Void> response = deleteDepartmentUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}