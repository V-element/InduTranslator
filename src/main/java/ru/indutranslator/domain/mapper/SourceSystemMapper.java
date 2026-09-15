package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.SourceSystemDto;
import ru.indutranslator.domain.entity.enterprise.SourceSystem;

@Mapper(componentModel = "spring")
public interface SourceSystemMapper {

    SourceSystemMapper INSTANCE = Mappers.getMapper(SourceSystemMapper.class);

    SourceSystemDto toDto(SourceSystem system);
    SourceSystem toEntity(SourceSystemDto systemDto);
}
