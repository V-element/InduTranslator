package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.RouteStepDto;
import ru.indutranslator.domain.entity.enterprise.RouteStep;
import ru.indutranslator.domain.mapper.RouteStepMapper;
import ru.indutranslator.domain.repository.RouteStepRepository;
import ru.indutranslator.service.RouteStepService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteStepServiceImpl implements RouteStepService {

    private final RouteStepRepository routeStepRepository;
    private final RouteStepMapper routeStepMapper;

    @Override
    @Transactional
    public RouteStepDto createStep(RouteStepDto stepDto, Long userId) {
        RouteStep step = routeStepMapper.toEntity(stepDto);
        RouteStep savedStep = routeStepRepository.save(step);
        return routeStepMapper.toDto(savedStep);
    }

    @Override
    @Transactional(readOnly = true)
    public RouteStepDto getStepById(Long id) {
        RouteStep step = routeStepRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Step not found: " + id));
        return routeStepMapper.toDto(step);
    }

    @Override
    @Transactional
    public RouteStepDto updateStep(Long id, RouteStepDto stepDto, Long userId) {
        RouteStep step = routeStepRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Step not found: " + id));

        step.setStatus(stepDto.getStatus());
        step.setName(stepDto.getName());

        RouteStep updatedStep = routeStepRepository.save(step);
        return routeStepMapper.toDto(updatedStep);
    }

    @Override
    @Transactional
    public void deleteStep(Long id) {
        routeStepRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteStepDto> getStepsByRoute(Long routeId) {
        return routeStepRepository.findByTaskRouteId(routeId).stream()
            .map(routeStepMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteStepDto> getStepsByDepartment(Long departmentId) {
        return routeStepRepository.findByDepartmentId(departmentId).stream()
            .map(routeStepMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteStepDto> getPendingSteps(Long userId) {
        return routeStepRepository.findByUserIdAndStatus(userId, "PENDING").stream()
            .map(routeStepMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void startStep(Long stepId, Long userId) {
        log.info("Starting step: {} by user: {}", stepId, userId);
    }

    @Override
    @Transactional
    public void completeStep(Long stepId, Long userId) {
        log.info("Completing step: {} by user: {}", stepId, userId);
    }

    @Override
    @Transactional
    public void skipStep(Long stepId, Long userId) {
        log.info("Skipping step: {} by user: {}", stepId, userId);
    }
}
