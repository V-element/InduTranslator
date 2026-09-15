package ru.indutranslator.service;

import ru.indutranslator.domain.dto.ClarificationDto;

import java.util.List;

public interface ClarificationService {

    ClarificationDto createClarification(ClarificationDto clarificationDto, Long userId);

    ClarificationDto getClarificationById(Long id);

    ClarificationDto updateClarification(Long id, ClarificationDto clarificationDto, Long userId);

    void deleteClarification(Long id, Long userId);

    List<ClarificationDto> getClarificationsByTask(Long taskId);

    List<ClarificationDto> getPendingClarifications();

    void answerClarification(Long id, String answer, Long userId);

    void closeClarification(Long id, Long userId);

    void reopenClarification(Long id, Long userId);
}
