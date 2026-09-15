package ru.indutranslator.service;

import ru.indutranslator.domain.dto.TaskAdaptationDto;

import java.util.List;

public interface TaskAdaptationService {

    TaskAdaptationDto createAdaptation(TaskAdaptationDto adaptationDto, Long userId);

    TaskAdaptationDto getAdaptationById(Long id);

    TaskAdaptationDto updateAdaptation(Long id, TaskAdaptationDto adaptationDto, Long userId);

    void deleteAdaptation(Long id, Long userId);

    List<TaskAdaptationDto> getAdaptationsByTask(Long taskId);

    List<TaskAdaptationDto> getAdaptationsByDepartment(Long departmentId);

    List<TaskAdaptationDto> getAdaptationsByRole(Long roleId);

    List<TaskAdaptationDto> getPendingAdaptations();

    void regenerateAdaptation(Long id, Long userId);

    void approveAdaptation(Long id, Long userId);

    void setFallbackFlag(Long id, Boolean isFallback, Long userId);
}
