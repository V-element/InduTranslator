package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.TaskRouteDto;
import ru.indutranslator.service.TaskRouteService;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@Tag(name = "Routes", description = "Task route management endpoints")
public class TaskRouteController {

    private final TaskRouteService taskRouteService;

    @PostMapping
    @Operation(summary = "Create new route")
    public ResponseEntity<TaskRouteDto> createRoute(@RequestBody TaskRouteDto routeDto) {
        TaskRouteDto createdRoute = taskRouteService.createRoute(routeDto, 1L);
        return ResponseEntity.ok(createdRoute);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get route by ID")
    public ResponseEntity<TaskRouteDto> getRoute(@PathVariable Long id) {
        return ResponseEntity.ok(taskRouteService.getRouteById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update route")
    public ResponseEntity<TaskRouteDto> updateRoute(@PathVariable Long id, @RequestBody TaskRouteDto routeDto) {
        return ResponseEntity.ok(taskRouteService.updateRoute(id, routeDto, 1L));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete route")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        taskRouteService.deleteRoute(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get routes by task")
    public ResponseEntity<List<TaskRouteDto>> getRoutesByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskRouteService.getRoutesByTask(taskId));
    }

    @GetMapping
    @Operation(summary = "Get all routes")
    public ResponseEntity<List<TaskRouteDto>> getAllRoutes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskRouteService.getAllRoutes(page, size));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active routes")
    public ResponseEntity<List<TaskRouteDto>> getActiveRoutes() {
        return ResponseEntity.ok(taskRouteService.getActiveRoutes());
    }
}
