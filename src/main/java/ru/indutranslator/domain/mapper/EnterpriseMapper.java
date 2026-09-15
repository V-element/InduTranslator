package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.EnterpriseDto;
import ru.indutranslator.domain.entity.enterprise.Enterprise;

@Mapper(componentModel = "spring")
public interface EnterpriseMapper {

    EnterpriseMapper INSTANCE = Mappers.getMapper(EnterpriseMapper.class);

    EnterpriseDto toDto(Enterprise enterprise);
    Enterprise toEntity(EnterpriseDto enterpriseDto);
}
