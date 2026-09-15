package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.ExecutionResultDto;
import ru.indutranslator.domain.entity.enterprise.ExecutionResult;
import ru.indutranslator.domain.mapper.ExecutionResultMapper;
import ru.indutranslator.domain.repository.ExecutionResultRepository;
import ru.indutranslator.service.ExecutionService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutionResultServiceImpl implements ExecutionService {

    private final ExecutionResultRepository executionResultRepository;
    private final ExecutionResultMapper executionResultMapper;

    @Override
    @Transactional
    public ExecutionResultDto createResult(ExecutionResultDto resultDto, Long userId) {
        ExecutionResult result = executionResultMapper.toEntity(resultDto);
        ExecutionResult savedResult = executionResultRepository.save(result);
        return executionResultMapper.toDto(savedResult);
    }

    @Override
    @Transactional(readOnly = true)
    public ExecutionResultDto getResultById(Long id) {
        ExecutionResult result = executionResultRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Execution result not found: " + id));
        return executionResultMapper.toDto(result);
    }

    @Override
    @Transactional
    public ExecutionResultDto updateResult(Long id, ExecutionResultDto resultDto, Long userId) {
        ExecutionResult result = executionResultRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Execution result not found: " + id));

        result.setStatus(resultDto.getStatus());
        result.setResultData(resultDto.getResultData());

        ExecutionResult updatedResult = executionResultRepository.save(result);
        return executionResultMapper.toDto(updatedResult);
    }

    @Override
    @Transactional
    public void deleteResult(Long id, Long userId) {
        if (!executionResultRepository.existsById(id)) {
            throw new RuntimeException("Execution result not found: " + id);
        }
        executionResultRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExecutionResultDto> getResultsByTask(Long taskId) {
        return executionResultRepository.findBySourceTaskId(taskId).stream()
            .map(executionResultMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExecutionResultDto> getResultsByStep(Long stepId) {
        return executionResultRepository.findByRouteStepId(stepId).stream()
            .map(executionResultMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExecutionResultDto> getPendingResults() {
        return executionResultRepository.findByStatus("PENDING").stream()
            .map(executionResultMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void completeStep(Long resultId, Long userId) {
        log.info("Completing step: {} by user: {}", resultId, userId);
    }

    @Override
    @Transactional
    public void acknowledgeResult(Long resultId, Long userId) {
        log.info("Acknowledging result: {} by user: {}", resultId, userId);
    }

    @Override
    @Transactional
    public void addComment(Long resultId, String comment, Long userId) {
        log.info("Adding comment to result {}: {}", resultId, comment);
    }

    @Override
    @Transactional
    public void attachFile(Long resultId, Long documentId, Long userId) {
        log.info("Attaching document {} to result {}", documentId, resultId);
    }
}
