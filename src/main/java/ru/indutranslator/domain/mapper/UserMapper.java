package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.UserDto;
import ru.indutranslator.domain.entity.enterprise.User;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, DepartmentMapper.class})
public interface UserMapper {

    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
