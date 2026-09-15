package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.RoiMetricDto;
import ru.indutranslator.service.RoiMetricService;

import java.util.List;

@RestController
@RequestMapping("/api/roi")
@RequiredArgsConstructor
@Tag(name = "ROI", description = "ROI metrics endpoints")
public class RoiMetricController {

    private final RoiMetricService roiMetricService;

    @PostMapping
    @Operation(summary = "Create new ROI metric")
    public ResponseEntity<RoiMetricDto> createMetric(@RequestBody RoiMetricDto metricDto) {
        RoiMetricDto createdMetric = roiMetricService.createMetric(metricDto, 1L);
        return ResponseEntity.ok(createdMetric);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ROI metric by ID")
    public ResponseEntity<RoiMetricDto> getMetric(@PathVariable Long id) {
        return ResponseEntity.ok(roiMetricService.getMetricById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ROI metric")
    public ResponseEntity<RoiMetricDto> updateMetric(
            @PathVariable Long id, @RequestBody RoiMetricDto metricDto) {
        return ResponseEntity.ok(roiMetricService.updateMetric(id, metricDto, 1L));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ROI metric")
    public ResponseEntity<Void> deleteMetric(@PathVariable Long id) {
        roiMetricService.deleteMetric(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get metrics by task")
    public ResponseEntity<List<RoiMetricDto>> getMetricsByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(roiMetricService.getMetricsByTask(taskId));
    }

    @GetMapping
    @Operation(summary = "Get all metrics")
    public ResponseEntity<List<RoiMetricDto>> getAllMetrics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(roiMetricService.getAllMetrics(page, size));
    }

    @GetMapping("/total")
    @Operation(summary = "Get total ROI")
    public ResponseEntity<Double> getTotalROI() {
        return ResponseEntity.ok(roiMetricService.getTotalROI());
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get metrics by date range")
    public ResponseEntity<List<RoiMetricDto>> getMetricsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return ResponseEntity.ok(roiMetricService.getMetricsByDateRange(startDate, endDate));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get metrics by department")
    public ResponseEntity<List<RoiMetricDto>> getMetricsByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(roiMetricService.getMetricsByDepartment(departmentId));
    }
}
