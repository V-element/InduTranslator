package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.SourceTaskDto;
import ru.indutranslator.domain.dto.TaskCreateDto;
import ru.indutranslator.domain.entity.enterprise.Enterprise;
import ru.indutranslator.domain.entity.enterprise.SourceSystem;
import ru.indutranslator.domain.entity.enterprise.SourceTask;
import ru.indutranslator.domain.repository.EnterpriseRepository;
import ru.indutranslator.domain.repository.SourceSystemRepository;
import ru.indutranslator.service.SourceTaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Source task management endpoints")
public class SourceTaskController {

    private final SourceTaskService sourceTaskService;
    private final EnterpriseRepository enterpriseRepository;
    private final SourceSystemRepository sourceSystemRepository;

    @PostMapping
    @Operation(summary = "Create new task")
    public ResponseEntity<SourceTaskDto> createTask(@RequestBody TaskCreateDto taskDto) {
        // Create entity directly with default enterprise and source system
        Enterprise enterprise = enterpriseRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("Enterprise not found"));
        SourceSystem sourceSystem = sourceSystemRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("SourceSystem not found"));
        
        SourceTask task = SourceTask.builder()
            .title(taskDto.getTitle())
            .description(taskDto.getDescription())
            .status(taskDto.getStatus())
            .priority(taskDto.getPriority() != null ? taskDto.getPriority() : 1)
            .enterprise(enterprise)
            .sourceSystem(sourceSystem)
            .build();
        SourceTask savedTask = sourceTaskService.saveTask(task);
        return ResponseEntity.ok(sourceTaskService.getTaskById(savedTask.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<SourceTaskDto> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(sourceTaskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task")
    public ResponseEntity<SourceTaskDto> updateTask(@PathVariable Long id, @RequestBody SourceTaskDto taskDto) {
        return ResponseEntity.ok(sourceTaskService.updateTask(id, taskDto, 1L));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        sourceTaskService.deleteTask(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all tasks with pagination")
    public ResponseEntity<Page<SourceTaskDto>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(required = false) String filter) {
        return ResponseEntity.ok(sourceTaskService.getAllTasks(page, size, sort, filter));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get tasks by status")
    public ResponseEntity<List<SourceTaskDto>> getTasksByStatus(@PathVariable String status) {
        return ResponseEntity.ok(sourceTaskService.getTasksByStatus(status));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get tasks by department")
    public ResponseEntity<List<SourceTaskDto>> getTasksByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(sourceTaskService.getTasksByDepartment(departmentId));
    }

    @GetMapping("/search")
    @Operation(summary = "Search tasks")
    public ResponseEntity<Page<SourceTaskDto>> searchTasks(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(sourceTaskService.searchTasks(query, page, size));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get task statistics")
    public ResponseEntity<String> getTaskStats() {
        return ResponseEntity.ok("Stats endpoint placeholder");
    }

    @PostMapping("/import")
    @Operation(summary = "Import tasks from CSV")
    public ResponseEntity<Integer> importTasks(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) throws Exception {
        int imported = sourceTaskService.importTasks(file.getInputStream());
        return ResponseEntity.ok(imported);
    }

    @GetMapping("/export")
    @Operation(summary = "Export tasks to CSV")
    public ResponseEntity<byte[]> exportTasks() throws Exception {
        byte[] csv = sourceTaskService.exportTasks();
        return ResponseEntity.ok()
            .header("Content-Type", "text/csv")
            .header("Content-Disposition", "attachment; filename=tasks.csv")
            .body(csv);
    }
}
