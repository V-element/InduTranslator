package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.AIConfigurationDto;
import ru.indutranslator.domain.entity.AIConfiguration;

@Mapper(componentModel = "spring")
public interface AIConfigurationMapper {

    AIConfigurationMapper INSTANCE = Mappers.getMapper(AIConfigurationMapper.class);

    AIConfigurationDto toDto(AIConfiguration config);
    AIConfiguration toEntity(AIConfigurationDto configDto);
}
