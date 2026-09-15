package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.AcknowledgementDto;
import ru.indutranslator.domain.entity.enterprise.Acknowledgement;
import ru.indutranslator.domain.mapper.AcknowledgementMapper;
import ru.indutranslator.domain.repository.AcknowledgementRepository;
import ru.indutranslator.service.AcknowledgementService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcknowledgementServiceImpl implements AcknowledgementService {

    private final AcknowledgementRepository acknowledgementRepository;
    private final AcknowledgementMapper acknowledgementMapper;

    @Override
    @Transactional
    public AcknowledgementDto createAcknowledgement(AcknowledgementDto acknowledgementDto, Long userId) {
        Acknowledgement acknowledgement = acknowledgementMapper.toEntity(acknowledgementDto);
        Acknowledgement savedAcknowledgement = acknowledgementRepository.save(acknowledgement);
        return acknowledgementMapper.toDto(savedAcknowledgement);
    }

    @Override
    @Transactional(readOnly = true)
    public AcknowledgementDto getAcknowledgementById(Long id) {
        Acknowledgement acknowledgement = acknowledgementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Acknowledgement not found: " + id));
        return acknowledgementMapper.toDto(acknowledgement);
    }

    @Override
    @Transactional
    public void deleteAcknowledgement(Long id) {
        acknowledgementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcknowledgementDto> getAcknowledgementsByResult(Long resultId) {
        return acknowledgementRepository.findByExecutionResultId(resultId).stream()
            .map(acknowledgementMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcknowledgementDto> getPendingAcknowledgements(Long userId) {
        return acknowledgementRepository.findByUserIdAndStatus(userId, "PENDING").stream()
            .map(acknowledgementMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void acknowledge(Long resultId, String comment, Long userId) {
        log.info("User {} acknowledging result {}: {}", userId, resultId, comment);
    }

    @Override
    @Transactional
    public void rejectAcknowledgement(Long id, String reason, Long userId) {
        log.info("User {} rejecting acknowledgement {}: {}", userId, id, reason);
    }
}
