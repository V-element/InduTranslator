package ru.indutranslator.service;

import ru.indutranslator.domain.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

    NotificationDto createNotification(NotificationDto notificationDto);

    NotificationDto getNotificationById(Long id);

    void deleteNotification(Long id);

    List<NotificationDto> getUserNotifications(Long userId, int page, int size);

    List<NotificationDto> getUnreadNotifications(Long userId);

    void markAsRead(Long id, Long userId);

    void markAllAsRead(Long userId);

    void sendNotification(Long userId, String title, String message, String type);
}
