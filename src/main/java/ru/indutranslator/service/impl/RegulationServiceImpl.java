package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.RegulationDto;
import ru.indutranslator.domain.entity.enterprise.Regulation;
import ru.indutranslator.domain.mapper.RegulationMapper;
import ru.indutranslator.domain.repository.RegulationRepository;
import ru.indutranslator.service.RegulationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegulationServiceImpl implements RegulationService {

    private final RegulationRepository regulationRepository;
    private final RegulationMapper regulationMapper;

    @Override
    @Transactional
    public RegulationDto createRegulation(RegulationDto regulationDto, Long userId) {
        Regulation regulation = regulationMapper.toEntity(regulationDto);
        Regulation savedRegulation = regulationRepository.save(regulation);
        return regulationMapper.toDto(savedRegulation);
    }

    @Override
    @Transactional(readOnly = true)
    public RegulationDto getRegulationById(Long id) {
        Regulation regulation = regulationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Regulation not found: " + id));
        return regulationMapper.toDto(regulation);
    }

    @Override
    @Transactional
    public RegulationDto updateRegulation(Long id, RegulationDto regulationDto, Long userId) {
        Regulation regulation = regulationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Regulation not found: " + id));

        regulation.setTitle(regulationDto.getTitle());
        regulation.setDescription(regulationDto.getDescription());
        regulation.setVersion(regulationDto.getVersion());
        regulation.setContent(regulationDto.getContent());

        Regulation updatedRegulation = regulationRepository.save(regulation);
        return regulationMapper.toDto(updatedRegulation);
    }

    @Override
    @Transactional
    public void deleteRegulation(Long id, Long userId) {
        if (!regulationRepository.existsById(id)) {
            throw new RuntimeException("Regulation not found: " + id);
        }
        regulationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegulationDto> getAllRegulations(int page, int size) {
        return regulationRepository.findAll().stream()
            .map(regulationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegulationDto> getRegulationsByDepartment(Long departmentId) {
        return regulationRepository.findByDepartmentId(departmentId).stream()
            .map(regulationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegulationDto> getActiveRegulations() {
        return regulationRepository.findByActiveTrue().stream()
            .map(regulationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegulationDto> getRegulationsByTaskRoute(Long routeId) {
        return regulationRepository.findByTaskRouteId(routeId).stream()
            .map(regulationMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void applyRegulationToRoute(Long regulationId, Long routeId, Long userId) {
        log.info("Applying regulation {} to route {}", regulationId, routeId);
    }

    @Override
    @Transactional
    public void removeRegulationFromRoute(Long regulationId, Long routeId, Long userId) {
        log.info("Removing regulation {} from route {}", regulationId, routeId);
    }
}
