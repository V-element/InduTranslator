package ru.indutranslator.service;

import ru.indutranslator.domain.dto.ExecutionResultDto;

import java.util.List;

public interface ExecutionService {

    ExecutionResultDto createResult(ExecutionResultDto resultDto, Long userId);

    ExecutionResultDto getResultById(Long id);

    ExecutionResultDto updateResult(Long id, ExecutionResultDto resultDto, Long userId);

    void deleteResult(Long id, Long userId);

    List<ExecutionResultDto> getResultsByTask(Long taskId);

    List<ExecutionResultDto> getResultsByStep(Long stepId);

    List<ExecutionResultDto> getPendingResults();

    void completeStep(Long resultId, Long userId);

    void acknowledgeResult(Long resultId, Long userId);

    void addComment(Long resultId, String comment, Long userId);

    void attachFile(Long resultId, Long documentId, Long userId);
}
