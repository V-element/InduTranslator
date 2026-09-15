package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.RoleDto;
import ru.indutranslator.domain.dto.UserDto;
import ru.indutranslator.domain.entity.enterprise.Role;
import ru.indutranslator.domain.entity.enterprise.User;
import ru.indutranslator.domain.mapper.RoleMapper;
import ru.indutranslator.domain.mapper.UserMapper;
import ru.indutranslator.domain.repository.RoleRepository;
import ru.indutranslator.domain.repository.UserRepository;
import ru.indutranslator.service.AdminService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto, Long adminId) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto, Long adminId) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found: " + id));
        
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id, Long adminId) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable, String filter, Boolean active, Long departmentId) {
        Page<User> users;
        if (filter != null && !filter.isEmpty()) {
            users = userRepository.findAllByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(filter, pageable);
        } else if (active != null) {
            users = userRepository.findAllByActive(active, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        return users.map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found: " + id));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto activateUser(Long id, Long adminId) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setActive(true);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto deactivateUser(Long id, Long adminId) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setActive(false);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public void assignRoleToUser(Long userId, Long roleId, Long adminId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId, Long adminId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        user.getRoles().remove(role);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getUserRoles(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return user.getRoles().stream()
            .map(Role::getName).toList();
    }

    @Override
    @Transactional
    public RoleDto createRole(RoleDto roleDto, Long adminId) {
        Role role = roleMapper.toEntity(roleDto);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Override
    @Transactional
    public RoleDto updateRole(Long roleId, RoleDto roleDto, Long adminId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());
        
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId, Long adminId) {
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found: " + roleId);
        }
        roleRepository.deleteById(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getAllRoles(int page, int size) {
        return roleRepository.findAll().stream()
            .map(roleMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getUserDepartments(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        if (user.getDepartment() != null) {
            return List.of(user.getDepartment().getId());
        }
        return List.of();
    }

    @Override
    @Transactional
    public void assignUserToDepartment(Long userId, Long departmentId, Long adminId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        // TODO: Implement department retrieval
        user.setDepartment(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeUserFromDepartment(Long userId, Long departmentId, Long adminId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        user.setDepartment(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateROICoefficient(String metricName, Double coefficient, Long adminId) {
        log.info("Updating ROI coefficient for {}: {}", metricName, coefficient);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getROICoefficient(String metricName) {
        return 1.0; // Default value
    }

    @Override
    @Transactional
    public void updateAIProviderConfig(String providerName, String apiKey, String baseUrl, Long adminId) {
        log.info("Updating AI provider config for {}: {}", providerName, apiKey);
    }

    @Override
    @Transactional
    public void activateAIProvider(Long configId, Long adminId) {
        log.info("Activating AI provider config: {}", configId);
    }

    @Override
    @Transactional
    public void updateSystemConfig(String key, String value, Long adminId) {
        log.info("Updating system config {}: {}", key, value);
    }

    @Override
    @Transactional(readOnly = true)
    public String getSystemConfig(String key) {
        return null; // Default value
    }
}
