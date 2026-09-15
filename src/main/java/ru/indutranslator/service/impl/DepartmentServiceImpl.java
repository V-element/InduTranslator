package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.DepartmentDto;
import ru.indutranslator.domain.entity.enterprise.Department;
import ru.indutranslator.domain.entity.enterprise.Regulation;
import ru.indutranslator.domain.entity.enterprise.User;
import ru.indutranslator.domain.mapper.DepartmentMapper;
import ru.indutranslator.domain.repository.DepartmentRepository;
import ru.indutranslator.domain.repository.RegulationRepository;
import ru.indutranslator.domain.repository.UserRepository;
import ru.indutranslator.service.DepartmentService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final RegulationRepository regulationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DepartmentDto createDepartment(DepartmentDto departmentDto, Long userId) {
        Department department = departmentMapper.toEntity(departmentDto);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found: " + id));
        return departmentMapper.toDto(department);
    }

    @Override
    @Transactional
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto, Long userId) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found: " + id));

        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        Department updatedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id, Long userId) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found: " + id);
        }
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDto> getAllDepartments(int page, int size) {
        return departmentRepository.findAll().stream()
            .map(departmentMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDto> getChildDepartments(Long parentId) {
        return departmentRepository.findByParentId(parentId).stream()
            .map(departmentMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDto> getDepartmentsByEnterprise(Long enterpriseId) {
        return departmentRepository.findByEnterpriseId(enterpriseId).stream()
            .map(departmentMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void assignRegulationToDepartment(Long departmentId, Long regulationId, Long userId) {
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new RuntimeException("Department not found: " + departmentId));
        Regulation regulation = regulationRepository.findById(regulationId)
            .orElseThrow(() -> new RuntimeException("Regulation not found: " + regulationId));
        
        log.info("Assigning regulation {} to department {}", regulationId, departmentId);
    }

    @Override
    @Transactional
    public void removeRegulationFromDepartment(Long departmentId, Long regulationId, Long userId) {
        log.info("Removing regulation {} from department {}", regulationId, departmentId);
    }

    @Override
    @Transactional
    public void assignCompetencyToDepartment(Long departmentId, Long competencyId, Long userId) {
        log.info("Assigning competency {} to department {}", competencyId, departmentId);
    }

    @Override
    @Transactional
    public void removeCompetencyFromDepartment(Long departmentId, Long competencyId, Long userId) {
        log.info("Removing competency {} from department {}", competencyId, departmentId);
    }
}
