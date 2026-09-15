package ru.indutranslator.service;

import ru.indutranslator.domain.dto.AIConfigurationDto;

import java.util.List;

public interface AIConfigurationService {

    AIConfigurationDto createConfiguration(AIConfigurationDto configDto, Long userId);

    AIConfigurationDto getConfigurationById(Long id);

    AIConfigurationDto updateConfiguration(Long id, AIConfigurationDto configDto, Long userId);

    void deleteConfiguration(Long id, Long userId);

    List<AIConfigurationDto> getAllConfigurations(int page, int size);

    AIConfigurationDto getActiveConfiguration();

    AIConfigurationDto setActiveConfiguration(Long id, Long userId);

    List<AIConfigurationDto> getProviderConfigurations(String provider);

    void testConnection(Long id, Long userId);
}
