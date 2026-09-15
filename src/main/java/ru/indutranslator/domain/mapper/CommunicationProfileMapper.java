package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.CommunicationProfileDto;
import ru.indutranslator.domain.entity.enterprise.CommunicationProfile;

@Mapper(componentModel = "spring")
public interface CommunicationProfileMapper {

    CommunicationProfileDto toDto(CommunicationProfile profile);
    CommunicationProfile toEntity(CommunicationProfileDto profileDto);
}
