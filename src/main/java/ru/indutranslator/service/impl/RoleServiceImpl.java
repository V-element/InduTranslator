package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.RoleDto;
import ru.indutranslator.domain.entity.enterprise.Role;
import ru.indutranslator.domain.entity.enterprise.User;
import ru.indutranslator.domain.mapper.RoleMapper;
import ru.indutranslator.domain.repository.RoleRepository;
import ru.indutranslator.domain.repository.UserRepository;
import ru.indutranslator.service.RoleService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RoleDto createRole(RoleDto roleDto, Long userId) {
        Role role = roleMapper.toEntity(roleDto);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found: " + id));
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional
    public RoleDto updateRole(Long id, RoleDto roleDto, Long userId) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found: " + id));

        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());

        Role updatedRole = roleRepository.save(role);
        return roleMapper.toDto(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long id, Long userId) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getAllRoles(int page, int size) {
        return roleRepository.findAll().stream()
            .map(roleMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getRolesByDepartment(Long departmentId) {
        return roleRepository.findAll().stream()
            .map(roleMapper::toDto).toList();
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
    public List<Long> getUsersWithRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        return role.getUsers().stream().map(User::getId).toList();
    }
}
