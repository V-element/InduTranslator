package ru.indutranslator.service;

import ru.indutranslator.domain.dto.TaskVersionDto;

import java.util.List;

public interface TaskVersionService {

    TaskVersionDto createVersion(TaskVersionDto versionDto, Long userId);

    TaskVersionDto getVersionById(Long id);

    void deleteVersion(Long id);

    List<TaskVersionDto> getVersionsByTask(Long taskId);

    List<TaskVersionDto> getAllVersions(int page, int size);

    TaskVersionDto getVersionByNumber(Long taskId, Integer versionNumber);

    void compareVersions(Long version1Id, Long version2Id);

    void restoreVersion(Long id, Long userId);
}
