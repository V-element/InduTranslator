package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.RoiMetricDto;
import ru.indutranslator.domain.entity.enterprise.RoiMetric;
import ru.indutranslator.domain.mapper.RoiMetricMapper;
import ru.indutranslator.domain.repository.RoiMetricRepository;
import ru.indutranslator.service.RoiMetricService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoiMetricServiceImpl implements RoiMetricService {

    private final RoiMetricRepository roiMetricRepository;
    private final RoiMetricMapper roiMetricMapper;

    @Override
    @Transactional
    public RoiMetricDto createMetric(RoiMetricDto metricDto, Long userId) {
        RoiMetric metric = roiMetricMapper.toEntity(metricDto);
        RoiMetric savedMetric = roiMetricRepository.save(metric);
        return roiMetricMapper.toDto(savedMetric);
    }

    @Override
    @Transactional(readOnly = true)
    public RoiMetricDto getMetricById(Long id) {
        RoiMetric metric = roiMetricRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Metric not found: " + id));
        return roiMetricMapper.toDto(metric);
    }

    @Override
    @Transactional
    public RoiMetricDto updateMetric(Long id, RoiMetricDto metricDto, Long userId) {
        RoiMetric metric = roiMetricRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Metric not found: " + id));

        metric.setCurrentValue(metricDto.getCurrentValue());
        metric.setBaselineValue(metricDto.getBaselineValue());

        RoiMetric updatedMetric = roiMetricRepository.save(metric);
        return roiMetricMapper.toDto(updatedMetric);
    }

    @Override
    @Transactional
    public void deleteMetric(Long id, Long userId) {
        if (!roiMetricRepository.existsById(id)) {
            throw new RuntimeException("Metric not found: " + id);
        }
        roiMetricRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoiMetricDto> getMetricsByTask(Long taskId) {
        return roiMetricRepository.findBySourceTaskId(taskId).stream()
            .map(roiMetricMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoiMetricDto> getAllMetrics(int page, int size) {
        return roiMetricRepository.findAll().stream()
            .map(roiMetricMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalROI() {
        List<RoiMetric> metrics = roiMetricRepository.findAll();
        return metrics.stream()
            .mapToDouble(m -> m.getImprovementValue() != null ? m.getImprovementValue() : 0.0)
            .sum();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoiMetricDto> getMetricsByDateRange(String startDate, String endDate) {
        return roiMetricRepository.findAll().stream()
            .map(roiMetricMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoiMetricDto> getMetricsByDepartment(Long departmentId) {
        return roiMetricRepository.findAll().stream()
            .map(roiMetricMapper::toDto).toList();
    }
}
