package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.DocumentDto;
import ru.indutranslator.domain.entity.Document;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    DocumentDto toDto(Document document);
    Document toEntity(DocumentDto documentDto);
}
