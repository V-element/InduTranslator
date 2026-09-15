package ru.indutranslator.service;

import ru.indutranslator.domain.dto.AcknowledgementDto;

import java.util.List;

public interface AcknowledgementService {

    AcknowledgementDto createAcknowledgement(AcknowledgementDto acknowledgementDto, Long userId);

    AcknowledgementDto getAcknowledgementById(Long id);

    void deleteAcknowledgement(Long id);

    List<AcknowledgementDto> getAcknowledgementsByResult(Long resultId);

    List<AcknowledgementDto> getPendingAcknowledgements(Long userId);

    void acknowledge(Long resultId, String comment, Long userId);

    void rejectAcknowledgement(Long id, String reason, Long userId);
}
