package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.IntegrationDto;
import ru.indutranslator.domain.entity.enterprise.Integration;
import ru.indutranslator.domain.entity.enterprise.User;
import ru.indutranslator.domain.mapper.IntegrationMapper;
import ru.indutranslator.domain.repository.IntegrationRepository;
import ru.indutranslator.domain.repository.UserRepository;
import ru.indutranslator.service.IntegrationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService {

    private final IntegrationRepository integrationRepository;
    private final IntegrationMapper integrationMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public IntegrationDto createIntegration(IntegrationDto integrationDto, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        Integration integration = integrationMapper.toEntity(integrationDto);
        integration.setEnterprise(user.getEnterprise());
        Integration savedIntegration = integrationRepository.save(integration);
        return integrationMapper.toDto(savedIntegration);
    }

    @Override
    @Transactional(readOnly = true)
    public IntegrationDto getIntegrationById(Long id) {
        Integration integration = integrationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Integration not found: " + id));
        return integrationMapper.toDto(integration);
    }

    @Override
    @Transactional
    public IntegrationDto updateIntegration(Long id, IntegrationDto integrationDto, String username) {
        Integration integration = integrationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Integration not found: " + id));

        integration.setName(integrationDto.getName());
        integration.setDescription(integrationDto.getDescription());
        integration.setActive(integrationDto.getActive());

        Integration updatedIntegration = integrationRepository.save(integration);
        return integrationMapper.toDto(updatedIntegration);
    }

    @Override
    @Transactional
    public void deleteIntegration(Long id, String username) {
        if (!integrationRepository.existsById(id)) {
            throw new RuntimeException("Integration not found: " + id);
        }
        integrationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IntegrationDto> getAllIntegrations(int page, int size) {
        return integrationRepository.findAll().stream()
            .map(integrationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IntegrationDto> getActiveIntegrations() {
        return integrationRepository.findByActiveTrue().stream()
            .map(integrationMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void testConnection(Long id, String username) {
        Integration integration = integrationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Integration not found: " + id));
        log.info("Testing connection for integration: {}", integration.getName());
    }

    @Override
    @Transactional
    public void syncData(Long id, String username) {
        Integration integration = integrationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Integration not found: " + id));
        log.info("Syncing data for integration: {}", integration.getName());
    }

    @Override
    @Transactional
    public void updateMapping(Long id, String mapping, String username) {
        Integration integration = integrationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Integration not found: " + id));
        integration.setConfig(mapping);
        integrationRepository.save(integration);
    }

    @Override
    @Transactional(readOnly = true)
    public IntegrationDto getLastSyncStatus(Long id) {
        return integrationRepository.findById(id)
            .map(integrationMapper::toDto)
            .orElseThrow(() -> new RuntimeException("Integration not found: " + id));
    }
}
