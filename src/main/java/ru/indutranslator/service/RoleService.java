package ru.indutranslator.service;

import ru.indutranslator.domain.dto.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto createRole(RoleDto roleDto, Long userId);

    RoleDto getRoleById(Long id);

    RoleDto updateRole(Long id, RoleDto roleDto, Long userId);

    void deleteRole(Long id, Long userId);

    List<RoleDto> getAllRoles(int page, int size);

    List<RoleDto> getRolesByDepartment(Long departmentId);

    void assignRoleToUser(Long userId, Long roleId, Long adminId);

    void removeRoleFromUser(Long userId, Long roleId, Long adminId);

    List<Long> getUsersWithRole(Long roleId);
}
