package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.AnalyticsDto;
import ru.indutranslator.service.AnalyticsService;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Analytics and reporting endpoints")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    @Operation(summary = "Get dashboard analytics")
    public ResponseEntity<AnalyticsDto> getDashboardAnalytics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long departmentId) {
        return ResponseEntity.ok(analyticsService.getDashboardAnalytics(startDate, endDate, departmentId));
    }

    @GetMapping("/tasks")
    @Operation(summary = "Get task analytics")
    public ResponseEntity<AnalyticsDto> getTaskAnalytics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(analyticsService.getTaskAnalytics(startDate, endDate));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get department analytics")
    public ResponseEntity<AnalyticsDto> getDepartmentAnalytics(
            @PathVariable Long departmentId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(analyticsService.getDepartmentAnalytics(departmentId, startDate, endDate));
    }

    @GetMapping("/time-series")
    @Operation(summary = "Get time series data")
    public ResponseEntity<AnalyticsDto> getTimeSeriesData(
            @RequestParam String period,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return ResponseEntity.ok(analyticsService.getTimeSeriesData(period, startDate, endDate));
    }

    @GetMapping("/roi")
    @Operation(summary = "Get ROI analytics")
    public ResponseEntity<Map<String, Object>> getROIAnalytics(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(analyticsService.getROIAnalytics(departmentId, startDate, endDate));
    }

    @GetMapping("/quality")
    @Operation(summary = "Get quality analytics")
    public ResponseEntity<Map<String, Object>> getQualityAnalytics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(analyticsService.getQualityAnalytics(startDate, endDate));
    }

    @GetMapping("/communication")
    @Operation(summary = "Get communication analytics")
    public ResponseEntity<Map<String, Object>> getCommunicationAnalytics(
            @RequestParam Long departmentId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(analyticsService.getCommunicationAnalytics(departmentId, startDate, endDate));
    }
}
