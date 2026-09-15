package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.TaskAdaptationDto;
import ru.indutranslator.domain.entity.enterprise.TaskAdaptation;
import ru.indutranslator.domain.mapper.TaskAdaptationMapper;
import ru.indutranslator.domain.repository.TaskAdaptationRepository;
import ru.indutranslator.service.TaskAdaptationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskAdaptationServiceImpl implements TaskAdaptationService {

    private final TaskAdaptationRepository taskAdaptationRepository;
    private final TaskAdaptationMapper taskAdaptationMapper;

    @Override
    @Transactional
    public TaskAdaptationDto createAdaptation(TaskAdaptationDto adaptationDto, Long userId) {
        TaskAdaptation adaptation = taskAdaptationMapper.toEntity(adaptationDto);
        TaskAdaptation savedAdaptation = taskAdaptationRepository.save(adaptation);
        return taskAdaptationMapper.toDto(savedAdaptation);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskAdaptationDto getAdaptationById(Long id) {
        TaskAdaptation adaptation = taskAdaptationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Adaptation not found: " + id));
        return taskAdaptationMapper.toDto(adaptation);
    }

    @Override
    @Transactional
    public TaskAdaptationDto updateAdaptation(Long id, TaskAdaptationDto adaptationDto, Long userId) {
        TaskAdaptation adaptation = taskAdaptationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Adaptation not found: " + id));

        adaptation.setStatus(adaptationDto.getStatus());
        adaptation.setAdaptedContent(adaptationDto.getAdaptedContent());

        TaskAdaptation updatedAdaptation = taskAdaptationRepository.save(adaptation);
        return taskAdaptationMapper.toDto(updatedAdaptation);
    }

    @Override
    @Transactional
    public void deleteAdaptation(Long id, Long userId) {
        if (!taskAdaptationRepository.existsById(id)) {
            throw new RuntimeException("Adaptation not found: " + id);
        }
        taskAdaptationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskAdaptationDto> getAdaptationsByTask(Long taskId) {
        return taskAdaptationRepository.findBySourceTaskId(taskId).stream()
            .map(taskAdaptationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskAdaptationDto> getAdaptationsByDepartment(Long departmentId) {
        return taskAdaptationRepository.findByDepartmentId(departmentId).stream()
            .map(taskAdaptationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskAdaptationDto> getAdaptationsByRole(Long roleId) {
        return taskAdaptationRepository.findByRoleId(roleId).stream()
            .map(taskAdaptationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskAdaptationDto> getPendingAdaptations() {
        return taskAdaptationRepository.findByStatus("PENDING").stream()
            .map(taskAdaptationMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void regenerateAdaptation(Long id, Long userId) {
        log.info("Regenerating adaptation: {} by user: {}", id, userId);
    }

    @Override
    @Transactional
    public void approveAdaptation(Long id, Long userId) {
        TaskAdaptation adaptation = taskAdaptationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Adaptation not found: " + id));
        adaptation.setStatus("APPROVED");
        taskAdaptationRepository.save(adaptation);
    }

    @Override
    @Transactional
    public void setFallbackFlag(Long id, Boolean isFallback, Long userId) {
        TaskAdaptation adaptation = taskAdaptationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Adaptation not found: " + id));
        adaptation.setFallback(isFallback);
        taskAdaptationRepository.save(adaptation);
    }
}
