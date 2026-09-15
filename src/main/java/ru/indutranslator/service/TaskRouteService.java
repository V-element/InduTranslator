package ru.indutranslator.service;

import ru.indutranslator.domain.dto.TaskRouteDto;

import java.util.List;

public interface TaskRouteService {

    TaskRouteDto createRoute(TaskRouteDto routeDto, Long userId);

    TaskRouteDto getRouteById(Long id);

    TaskRouteDto updateRoute(Long id, TaskRouteDto routeDto, Long userId);

    void deleteRoute(Long id, Long userId);

    List<TaskRouteDto> getRoutesByTask(Long taskId);

    List<TaskRouteDto> getAllRoutes(int page, int size);

    List<TaskRouteDto> getActiveRoutes();

    void validateRoute(Long id);
}
