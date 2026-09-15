package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.AcknowledgementDto;
import ru.indutranslator.domain.entity.enterprise.Acknowledgement;

@Mapper(componentModel = "spring")
public interface AcknowledgementMapper {

    AcknowledgementDto toDto(Acknowledgement acknowledgement);
    Acknowledgement toEntity(AcknowledgementDto acknowledgementDto);
}
