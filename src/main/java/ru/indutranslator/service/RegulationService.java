package ru.indutranslator.service;

import ru.indutranslator.domain.dto.RegulationDto;

import java.util.List;

public interface RegulationService {

    RegulationDto createRegulation(RegulationDto regulationDto, Long userId);

    RegulationDto getRegulationById(Long id);

    RegulationDto updateRegulation(Long id, RegulationDto regulationDto, Long userId);

    void deleteRegulation(Long id, Long userId);

    List<RegulationDto> getAllRegulations(int page, int size);

    List<RegulationDto> getRegulationsByDepartment(Long departmentId);

    List<RegulationDto> getActiveRegulations();

    List<RegulationDto> getRegulationsByTaskRoute(Long routeId);

    void applyRegulationToRoute(Long regulationId, Long routeId, Long userId);

    void removeRegulationFromRoute(Long regulationId, Long routeId, Long userId);
}
