package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.NotificationDto;
import ru.indutranslator.domain.entity.enterprise.Notification;
import ru.indutranslator.domain.mapper.NotificationMapper;
import ru.indutranslator.domain.repository.NotificationRepository;
import ru.indutranslator.service.NotificationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public NotificationDto createNotification(NotificationDto notificationDto) {
        Notification notification = notificationMapper.toEntity(notificationDto);
        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toDto(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationDto getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        return notificationMapper.toDto(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getUserNotifications(Long userId, int page, int size) {
        return notificationRepository.findByRecipientUserId(userId).stream()
            .map(notificationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getUnreadNotifications(Long userId) {
        return notificationRepository.findByRecipientUserIdAndReadFalse(userId).stream()
            .map(notificationMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void markAsRead(Long id, Long userId) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByRecipientUserIdAndReadFalse(userId);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    @Transactional
    public void sendNotification(Long userId, String title, String message, String type) {
        NotificationDto notificationDto = NotificationDto.builder()
            .userId(userId)
            .title(title)
            .message(message)
            .type(type)
            .read(false)
            .build();
        createNotification(notificationDto);
    }
}
