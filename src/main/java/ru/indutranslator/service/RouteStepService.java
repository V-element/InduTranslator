package ru.indutranslator.service;

import ru.indutranslator.domain.dto.RouteStepDto;

import java.util.List;

public interface RouteStepService {

    RouteStepDto createStep(RouteStepDto stepDto, Long userId);

    RouteStepDto getStepById(Long id);

    RouteStepDto updateStep(Long id, RouteStepDto stepDto, Long userId);

    void deleteStep(Long id);

    List<RouteStepDto> getStepsByRoute(Long routeId);

    List<RouteStepDto> getStepsByDepartment(Long departmentId);

    List<RouteStepDto> getPendingSteps(Long userId);

    void startStep(Long stepId, Long userId);

    void completeStep(Long stepId, Long userId);

    void skipStep(Long stepId, Long userId);
}
