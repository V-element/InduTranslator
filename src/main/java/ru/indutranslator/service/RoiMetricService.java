package ru.indutranslator.service;

import ru.indutranslator.domain.dto.RoiMetricDto;

import java.util.List;

public interface RoiMetricService {

    RoiMetricDto createMetric(RoiMetricDto metricDto, Long userId);

    RoiMetricDto getMetricById(Long id);

    RoiMetricDto updateMetric(Long id, RoiMetricDto metricDto, Long userId);

    void deleteMetric(Long id, Long userId);

    List<RoiMetricDto> getMetricsByTask(Long taskId);

    List<RoiMetricDto> getAllMetrics(int page, int size);

    Double getTotalROI();

    List<RoiMetricDto> getMetricsByDateRange(String startDate, String endDate);

    List<RoiMetricDto> getMetricsByDepartment(Long departmentId);
}
