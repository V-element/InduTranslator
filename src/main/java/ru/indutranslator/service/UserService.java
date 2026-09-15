package ru.indutranslator.service;

import ru.indutranslator.domain.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto, Long adminId);

    UserDto updateUser(Long id, UserDto userDto, Long adminId);

    void deleteUser(Long id, Long adminId);

    UserDto getUserById(Long id);

    UserDto getUserByUsername(String username);

    UserDto getUserByEmail(String email);

    List<UserDto> getAllUsers(int page, int size);

    UserDto getCurrentUser();
}
