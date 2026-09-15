package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.DepartmentDto;
import ru.indutranslator.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "Department management endpoints")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @Operation(summary = "Create new department")
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto) {
        DepartmentDto createdDepartment = departmentService.createDepartment(departmentDto, 1L);
        return ResponseEntity.ok(createdDepartment);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department")
    public ResponseEntity<DepartmentDto> updateDepartment(
            @PathVariable Long id, @RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDto, 1L));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all departments")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(departmentService.getAllDepartments(page, size));
    }

    @GetMapping("/parent/{parentId}")
    @Operation(summary = "Get child departments")
    public ResponseEntity<List<DepartmentDto>> getChildDepartments(@PathVariable Long parentId) {
        return ResponseEntity.ok(departmentService.getChildDepartments(parentId));
    }

    @GetMapping("/enterprise/{enterpriseId}")
    @Operation(summary = "Get departments by enterprise")
    public ResponseEntity<List<DepartmentDto>> getDepartmentsByEnterprise(@PathVariable Long enterpriseId) {
        return ResponseEntity.ok(departmentService.getDepartmentsByEnterprise(enterpriseId));
    }

    @PostMapping("/{departmentId}/regulation/{regulationId}/assign")
    @Operation(summary = "Assign regulation to department")
    public ResponseEntity<Void> assignRegulationToDepartment(
            @PathVariable Long departmentId, @PathVariable Long regulationId) {
        departmentService.assignRegulationToDepartment(departmentId, regulationId, 1L);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{departmentId}/regulation/{regulationId}/remove")
    @Operation(summary = "Remove regulation from department")
    public ResponseEntity<Void> removeRegulationFromDepartment(
            @PathVariable Long departmentId, @PathVariable Long regulationId) {
        departmentService.removeRegulationFromDepartment(departmentId, regulationId, 1L);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{departmentId}/competency/{competencyId}/assign")
    @Operation(summary = "Assign competency to department")
    public ResponseEntity<Void> assignCompetencyToDepartment(
            @PathVariable Long departmentId, @PathVariable Long competencyId) {
        departmentService.assignCompetencyToDepartment(departmentId, competencyId, 1L);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{departmentId}/competency/{competencyId}/remove")
    @Operation(summary = "Remove competency from department")
    public ResponseEntity<Void> removeCompetencyFromDepartment(
            @PathVariable Long departmentId, @PathVariable Long competencyId) {
        departmentService.removeCompetencyFromDepartment(departmentId, competencyId, 1L);
        return ResponseEntity.ok().build();
    }
}
