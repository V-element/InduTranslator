package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.DepartmentDto;
import ru.indutranslator.domain.entity.enterprise.Department;

@Mapper(componentModel = "spring", uses = CommunicationProfileMapper.class)
public interface DepartmentMapper {

    DepartmentDto toDto(Department department);
    Department toEntity(DepartmentDto departmentDto);
}
