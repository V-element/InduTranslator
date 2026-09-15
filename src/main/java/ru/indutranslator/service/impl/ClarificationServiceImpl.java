package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.ClarificationDto;
import ru.indutranslator.domain.entity.enterprise.Clarification;
import ru.indutranslator.domain.mapper.ClarificationMapper;
import ru.indutranslator.domain.repository.ClarificationRepository;
import ru.indutranslator.service.ClarificationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClarificationServiceImpl implements ClarificationService {

    private final ClarificationRepository clarificationRepository;
    private final ClarificationMapper clarificationMapper;

    @Override
    @Transactional
    public ClarificationDto createClarification(ClarificationDto clarificationDto, Long userId) {
        Clarification clarification = clarificationMapper.toEntity(clarificationDto);
        Clarification savedClarification = clarificationRepository.save(clarification);
        return clarificationMapper.toDto(savedClarification);
    }

    @Override
    @Transactional(readOnly = true)
    public ClarificationDto getClarificationById(Long id) {
        Clarification clarification = clarificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Clarification not found: " + id));
        return clarificationMapper.toDto(clarification);
    }

    @Override
    @Transactional
    public ClarificationDto updateClarification(Long id, ClarificationDto clarificationDto, Long userId) {
        Clarification clarification = clarificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Clarification not found: " + id));

        clarification.setAnswer(clarificationDto.getAnswer());
        clarification.setStatus(clarificationDto.getStatus());

        Clarification updatedClarification = clarificationRepository.save(clarification);
        return clarificationMapper.toDto(updatedClarification);
    }

    @Override
    @Transactional
    public void deleteClarification(Long id, Long userId) {
        if (!clarificationRepository.existsById(id)) {
            throw new RuntimeException("Clarification not found: " + id);
        }
        clarificationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClarificationDto> getClarificationsByTask(Long taskId) {
        return clarificationRepository.findBySourceTaskId(taskId).stream()
            .map(clarificationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClarificationDto> getPendingClarifications() {
        return clarificationRepository.findByStatus("PENDING").stream()
            .map(clarificationMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void answerClarification(Long id, String answer, Long userId) {
        Clarification clarification = clarificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Clarification not found: " + id));
        clarification.setAnswer(answer);
        clarification.setStatus("ANSWERED");
        clarificationRepository.save(clarification);
    }

    @Override
    @Transactional
    public void closeClarification(Long id, Long userId) {
        log.info("Closing clarification: {}", id);
    }

    @Override
    @Transactional
    public void reopenClarification(Long id, Long userId) {
        log.info("Reopening clarification: {}", id);
    }
}
