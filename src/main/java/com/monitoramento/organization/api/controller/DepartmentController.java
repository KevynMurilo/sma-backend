package com.monitoramento.organization.api.controller;

import com.monitoramento.organization.api.dto.DepartmentDTO;
import com.monitoramento.organization.domain.useCase.department.CreateDepartmentUseCase;
import com.monitoramento.organization.domain.useCase.department.DeleteDepartmentUseCase;
import com.monitoramento.organization.domain.useCase.department.GetAllDepartmentUseCase;
import com.monitoramento.organization.domain.useCase.department.UpdateDepartmentUseCase;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final CreateDepartmentUseCase createDepartmentUseCase;
    private final GetAllDepartmentUseCase getAllDepartmentUseCase;
    private final DeleteDepartmentUseCase deleteDepartmentUseCase;
    private final UpdateDepartmentUseCase updateDepartmentUseCase;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<DepartmentDTO>>> getAll(Pageable pageable) {
        ApiResponseDTO<PagedResponseDTO<DepartmentDTO>> response = getAllDepartmentUseCase.execute(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<DepartmentDTO>> createDepartment(@RequestBody DepartmentDTO dto) {
        ApiResponseDTO<DepartmentDTO> response = createDepartmentUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<DepartmentDTO>> updateDepartment(@PathVariable UUID id, @RequestBody DepartmentDTO dto) {
        ApiResponseDTO<DepartmentDTO> response = updateDepartmentUseCase.execute(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteDepartment(@PathVariable UUID id) {
        ApiResponseDTO<Void> response = deleteDepartmentUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
