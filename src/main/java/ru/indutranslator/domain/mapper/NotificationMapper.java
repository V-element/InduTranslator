package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.NotificationDto;
import ru.indutranslator.domain.entity.enterprise.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDto toDto(Notification notification);
    Notification toEntity(NotificationDto notificationDto);
}
