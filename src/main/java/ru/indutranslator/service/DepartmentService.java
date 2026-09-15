package ru.indutranslator.service;

import ru.indutranslator.domain.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService {

    DepartmentDto createDepartment(DepartmentDto departmentDto, Long userId);

    DepartmentDto getDepartmentById(Long id);

    DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto, Long userId);

    void deleteDepartment(Long id, Long userId);

    List<DepartmentDto> getAllDepartments(int page, int size);

    List<DepartmentDto> getChildDepartments(Long parentId);

    List<DepartmentDto> getDepartmentsByEnterprise(Long enterpriseId);

    void assignRegulationToDepartment(Long departmentId, Long regulationId, Long userId);

    void removeRegulationFromDepartment(Long departmentId, Long regulationId, Long userId);

    void assignCompetencyToDepartment(Long departmentId, Long competencyId, Long userId);

    void removeCompetencyFromDepartment(Long departmentId, Long competencyId, Long userId);
}
