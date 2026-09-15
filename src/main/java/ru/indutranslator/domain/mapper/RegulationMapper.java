package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.RegulationDto;
import ru.indutranslator.domain.entity.enterprise.Regulation;

@Mapper(componentModel = "spring")
public interface RegulationMapper {

    RegulationDto toDto(Regulation regulation);
    Regulation toEntity(RegulationDto regulationDto);
}
