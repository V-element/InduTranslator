package ru.indutranslator.service;

import ru.indutranslator.domain.dto.AnalyticsDto;

import java.util.Map;

public interface AnalyticsService {

    AnalyticsDto getDashboardAnalytics(String startDate, String endDate, Long departmentId);

    AnalyticsDto getTaskAnalytics(String startDate, String endDate);

    AnalyticsDto getDepartmentAnalytics(Long departmentId, String startDate, String endDate);

    AnalyticsDto getTimeSeriesData(String period, String startDate, String endDate);

    Map<String, Object> getROIAnalytics(Long departmentId, String startDate, String endDate);

    Map<String, Object> getQualityAnalytics(String startDate, String endDate);

    Map<String, Object> getCommunicationAnalytics(Long departmentId, String startDate, String endDate);
}
