package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.TaskVersionDto;
import ru.indutranslator.domain.entity.enterprise.TaskVersion;
import ru.indutranslator.domain.mapper.TaskVersionMapper;
import ru.indutranslator.domain.repository.TaskVersionRepository;
import ru.indutranslator.service.TaskVersionService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskVersionServiceImpl implements TaskVersionService {

    private final TaskVersionRepository taskVersionRepository;
    private final TaskVersionMapper taskVersionMapper;

    @Override
    @Transactional
    public TaskVersionDto createVersion(TaskVersionDto versionDto, Long userId) {
        TaskVersion version = taskVersionMapper.toEntity(versionDto);
        TaskVersion savedVersion = taskVersionRepository.save(version);
        return taskVersionMapper.toDto(savedVersion);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskVersionDto getVersionById(Long id) {
        TaskVersion version = taskVersionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Version not found: " + id));
        return taskVersionMapper.toDto(version);
    }

    @Override
    @Transactional
    public void deleteVersion(Long id) {
        taskVersionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskVersionDto> getVersionsByTask(Long taskId) {
        return taskVersionRepository.findBySourceTaskId(taskId).stream()
            .map(taskVersionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskVersionDto> getAllVersions(int page, int size) {
        return taskVersionRepository.findAll().stream()
            .map(taskVersionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskVersionDto getVersionByNumber(Long taskId, Integer versionNumber) {
        return taskVersionRepository.findBySourceTaskIdAndVersionNumber(taskId, versionNumber)
            .map(taskVersionMapper::toDto)
            .orElseThrow(() -> new RuntimeException("Version not found: task=" + taskId + ", version=" + versionNumber));
    }

    @Override
    @Transactional
    public void compareVersions(Long version1Id, Long version2Id) {
        log.info("Comparing versions: {} and {}", version1Id, version2Id);
    }

    @Override
    @Transactional
    public void restoreVersion(Long id, Long userId) {
        log.info("Restoring version {} by user {}", id, userId);
    }
}
