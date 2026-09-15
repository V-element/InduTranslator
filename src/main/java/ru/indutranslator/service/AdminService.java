package ru.indutranslator.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.indutranslator.domain.dto.RoleDto;
import ru.indutranslator.domain.dto.UserDto;

import java.util.List;

public interface AdminService {

    // User management
    UserDto createUser(UserDto userDto, Long adminId);

    UserDto updateUser(Long id, UserDto userDto, Long adminId);

    void deleteUser(Long id, Long adminId);

    Page<UserDto> getAllUsers(Pageable pageable, String filter, Boolean active, Long departmentId);

    UserDto getUserById(Long id);

    UserDto activateUser(Long id, Long adminId);

    UserDto deactivateUser(Long id, Long adminId);

    // Role management
    void assignRoleToUser(Long userId, Long roleId, Long adminId);

    void removeRoleFromUser(Long userId, Long roleId, Long adminId);

    List<String> getUserRoles(Long userId);

    RoleDto createRole(RoleDto roleDto, Long adminId);

    RoleDto updateRole(Long roleId, RoleDto roleDto, Long adminId);

    void deleteRole(Long roleId, Long adminId);

    List<RoleDto> getAllRoles(int page, int size);

    // Department management
    List<Long> getUserDepartments(Long userId);

    void assignUserToDepartment(Long userId, Long departmentId, Long adminId);

    void removeUserFromDepartment(Long userId, Long departmentId, Long adminId);

    // ROI configuration
    void updateROICoefficient(String metricName, Double coefficient, Long adminId);

    Double getROICoefficient(String metricName);

    // AI configuration
    void updateAIProviderConfig(String providerName, String apiKey, String baseUrl, Long adminId);

    void activateAIProvider(Long configId, Long adminId);

    // System configuration
    void updateSystemConfig(String key, String value, Long adminId);

    String getSystemConfig(String key);
}
