package ru.indutranslator.service;

import ru.indutranslator.domain.dto.IntegrationDto;

import java.util.List;

public interface IntegrationService {

    IntegrationDto createIntegration(IntegrationDto integrationDto, String username);

    IntegrationDto getIntegrationById(Long id);

    IntegrationDto updateIntegration(Long id, IntegrationDto integrationDto, String username);

    void deleteIntegration(Long id, String username);

    List<IntegrationDto> getAllIntegrations(int page, int size);

    List<IntegrationDto> getActiveIntegrations();

    void testConnection(Long id, String username);

    void syncData(Long id, String username);

    void updateMapping(Long id, String mapping, String username);

    IntegrationDto getLastSyncStatus(Long id);
}
