package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.SourceTaskDto;
import ru.indutranslator.domain.entity.enterprise.SourceTask;
import ru.indutranslator.domain.mapper.SourceTaskMapper;
import ru.indutranslator.domain.repository.SourceTaskRepository;
import ru.indutranslator.service.SourceTaskService;

import java.io.*;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceTaskServiceImpl implements SourceTaskService {

    private final SourceTaskRepository sourceTaskRepository;
    private final SourceTaskMapper sourceTaskMapper;

    @Override
    @Transactional
    public SourceTaskDto createTask(SourceTaskDto taskDto, Long userId) {
        SourceTask task = sourceTaskMapper.toEntity(taskDto);
        SourceTask savedTask = sourceTaskRepository.save(task);
        return sourceTaskMapper.toDto(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public SourceTaskDto getTaskById(Long id) {
        SourceTask task = sourceTaskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found: " + id));
        return sourceTaskMapper.toDto(task);
    }

    @Override
    @Transactional
    public SourceTaskDto updateTask(Long id, SourceTaskDto taskDto, Long userId) {
        SourceTask task = sourceTaskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found: " + id));

        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());

        SourceTask updatedTask = sourceTaskRepository.save(task);
        return sourceTaskMapper.toDto(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id, Long userId) {
        if (!sourceTaskRepository.existsById(id)) {
            throw new RuntimeException("Task not found: " + id);
        }
        sourceTaskRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SourceTaskDto> getAllTasks(int page, int size, String sort, String filter) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<SourceTask> taskPage = sourceTaskRepository.findAll(pageable);
        return taskPage.map(sourceTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SourceTaskDto> getTasksByStatus(String status) {
        return sourceTaskRepository.findByStatus(status).stream()
            .map(sourceTaskMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SourceTaskDto> getTasksByDepartment(Long departmentId) {
        // Implementation for department-based filtering
        return sourceTaskRepository.findAll().stream()
            .map(sourceTaskMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SourceTaskDto> searchTasks(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SourceTask> taskPage = sourceTaskRepository.findByTitleContainingOrDescriptionContaining(query, query, pageable);
        return taskPage.map(sourceTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByStatus(String status) {
        return sourceTaskRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getStatuses() {
        return sourceTaskRepository.findDistinctStatuses();
    }

    @Override
    @Transactional
    public SourceTask saveTask(SourceTask task) {
        return sourceTaskRepository.save(task);
    }

    @Override
    @Transactional
    public int importTasks(InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int imported = 0;
        
        // Skip header line
        reader.readLine();
        
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                SourceTaskDto dto = new SourceTaskDto();
                dto.setTitle(parts[0].trim());
                dto.setDescription(parts.length > 1 ? parts[1].trim() : "");
                dto.setStatus(parts.length > 2 ? parts[2].trim() : "pending");
                if (parts.length > 3) {
                    try {
                        dto.setPriority(Integer.parseInt(parts[3].trim()));
                    } catch (NumberFormatException e) {
                        dto.setPriority(1);
                    }
                }
                createTask(dto, 1L);
                imported++;
            }
        }
        return imported;
    }

    @Override
    public byte[] exportTasks() throws Exception {
        StringBuilder csv = new StringBuilder();
        csv.append("Title,Description,Status,Priority\n");
        
        List<SourceTaskDto> tasks = sourceTaskRepository.findAll().stream()
            .map(sourceTaskMapper::toDto).toList();
        
        for (SourceTaskDto task : tasks) {
            csv.append(task.getTitle()).append(",")
               .append(task.getDescription() != null ? task.getDescription().replace(",", " ") : "").append(",")
               .append(task.getStatus()).append(",")
               .append(task.getPriority()).append("\n");
        }
        
        return csv.toString().getBytes("UTF-8");
    }
}
