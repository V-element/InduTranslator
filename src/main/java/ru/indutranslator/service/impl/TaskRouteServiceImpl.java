package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.TaskRouteDto;
import ru.indutranslator.domain.entity.enterprise.TaskRoute;
import ru.indutranslator.domain.mapper.TaskRouteMapper;
import ru.indutranslator.domain.repository.TaskRouteRepository;
import ru.indutranslator.service.TaskRouteService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRouteServiceImpl implements TaskRouteService {

    private final TaskRouteRepository taskRouteRepository;
    private final TaskRouteMapper taskRouteMapper;

    @Override
    @Transactional
    public TaskRouteDto createRoute(TaskRouteDto routeDto, Long userId) {
        TaskRoute route = taskRouteMapper.toEntity(routeDto);
        TaskRoute savedRoute = taskRouteRepository.save(route);
        return taskRouteMapper.toDto(savedRoute);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskRouteDto getRouteById(Long id) {
        TaskRoute route = taskRouteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Route not found: " + id));
        return taskRouteMapper.toDto(route);
    }

    @Override
    @Transactional
    public TaskRouteDto updateRoute(Long id, TaskRouteDto routeDto, Long userId) {
        TaskRoute route = taskRouteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Route not found: " + id));

        route.setName(routeDto.getName());
        route.setDescription(routeDto.getDescription());
        route.setStatus(routeDto.getStatus());

        TaskRoute updatedRoute = taskRouteRepository.save(route);
        return taskRouteMapper.toDto(updatedRoute);
    }

    @Override
    @Transactional
    public void deleteRoute(Long id, Long userId) {
        if (!taskRouteRepository.existsById(id)) {
            throw new RuntimeException("Route not found: " + id);
        }
        taskRouteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskRouteDto> getRoutesByTask(Long taskId) {
        return taskRouteRepository.findBySourceTaskId(taskId).stream()
            .map(taskRouteMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskRouteDto> getAllRoutes(int page, int size) {
        return taskRouteRepository.findAll().stream()
            .map(taskRouteMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskRouteDto> getActiveRoutes() {
        return taskRouteRepository.findByStatus("ACTIVE").stream()
            .map(taskRouteMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void validateRoute(Long id) {
        // Route validation logic
        log.info("Validating route: {}", id);
    }
}
