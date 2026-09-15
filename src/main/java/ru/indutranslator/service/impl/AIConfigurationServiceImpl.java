package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.AIConfigurationDto;
import ru.indutranslator.domain.entity.AIConfiguration;
import ru.indutranslator.domain.mapper.AIConfigurationMapper;
import ru.indutranslator.domain.repository.AIConfigurationRepository;
import ru.indutranslator.service.AIConfigurationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIConfigurationServiceImpl implements AIConfigurationService {

    private final AIConfigurationRepository aiConfigurationRepository;
    private final AIConfigurationMapper aiConfigurationMapper;

    @Override
    @Transactional
    public AIConfigurationDto createConfiguration(AIConfigurationDto configDto, Long userId) {
        AIConfiguration config = aiConfigurationMapper.toEntity(configDto);
        AIConfiguration savedConfig = aiConfigurationRepository.save(config);
        return aiConfigurationMapper.toDto(savedConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public AIConfigurationDto getConfigurationById(Long id) {
        AIConfiguration config = aiConfigurationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Configuration not found: " + id));
        return aiConfigurationMapper.toDto(config);
    }

    @Override
    @Transactional
    public AIConfigurationDto updateConfiguration(Long id, AIConfigurationDto configDto, Long userId) {
        AIConfiguration config = aiConfigurationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Configuration not found: " + id));

        config.setProvider(configDto.getProvider());
        config.setBaseUrl(configDto.getBaseUrl());
        config.setModel(configDto.getModel());

        AIConfiguration updatedConfig = aiConfigurationRepository.save(config);
        return aiConfigurationMapper.toDto(updatedConfig);
    }

    @Override
    @Transactional
    public void deleteConfiguration(Long id, Long userId) {
        if (!aiConfigurationRepository.existsById(id)) {
            throw new RuntimeException("Configuration not found: " + id);
        }
        aiConfigurationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AIConfigurationDto> getAllConfigurations(int page, int size) {
        return aiConfigurationRepository.findAll().stream()
            .map(aiConfigurationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AIConfigurationDto getActiveConfiguration() {
        return aiConfigurationRepository.findByActiveTrue()
            .map(aiConfigurationMapper::toDto)
            .orElseThrow(() -> new RuntimeException("No active configuration found"));
    }

    @Override
    @Transactional
    public AIConfigurationDto setActiveConfiguration(Long id, Long userId) {
        AIConfiguration config = aiConfigurationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Configuration not found: " + id));
        config.setActive(true);
        AIConfiguration savedConfig = aiConfigurationRepository.save(config);

        // Deactivate all other configurations
        aiConfigurationRepository.findAll().stream()
            .filter(c -> !c.getId().equals(id))
            .forEach(c -> c.setActive(false));
        
        return aiConfigurationMapper.toDto(savedConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AIConfigurationDto> getProviderConfigurations(String provider) {
        return aiConfigurationRepository.findByProvider(provider).stream()
            .map(aiConfigurationMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void testConnection(Long id, Long userId) {
        log.info("Testing connection for AI configuration: {}", id);
    }
}
