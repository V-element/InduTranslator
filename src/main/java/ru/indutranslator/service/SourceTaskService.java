package ru.indutranslator.service;

import org.springframework.data.domain.Page;
import ru.indutranslator.domain.dto.SourceTaskDto;
import ru.indutranslator.domain.entity.enterprise.SourceTask;

import java.io.InputStream;
import java.util.List;

public interface SourceTaskService {

    SourceTaskDto createTask(SourceTaskDto taskDto, Long userId);

    SourceTaskDto getTaskById(Long id);

    SourceTaskDto updateTask(Long id, SourceTaskDto taskDto, Long userId);

    void deleteTask(Long id, Long userId);

    Page<SourceTaskDto> getAllTasks(int page, int size, String sort, String filter);

    List<SourceTaskDto> getTasksByStatus(String status);

    List<SourceTaskDto> getTasksByDepartment(Long departmentId);

    Page<SourceTaskDto> searchTasks(String query, int page, int size);

    int countByStatus(String status);

    List<String> getStatuses();

    SourceTask saveTask(SourceTask task);

    int importTasks(InputStream inputStream) throws Exception;

    byte[] exportTasks() throws Exception;
}
