package com.spring.E_Learning.Service;


import com.spring.E_Learning.DTOs.Mapper.NotificationMapper;
import com.spring.E_Learning.DTOs.NotificationDto;
import com.spring.E_Learning.Model.Notification;
import com.spring.E_Learning.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final JavaMailSender emailService;

    public NotificationDto createNotification(NotificationDto notificationDto) {
        Notification notification = notificationMapper.toEntity(notificationDto);
        notification.setSent(false);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationMapper.toDto(notificationRepository.save(notification));
    }

    @Scheduled(fixedRate = 60000) // 60 sec
    public void sendPendingNotifications() {
        List<Notification> pending = notificationRepository.findBySentFalse();
        for (Notification n : pending) {
            try {
                sendEmail(n);
                n.setSent(true);
                notificationRepository.save(n);
            } catch (Exception ex) {
                throw new RuntimeException("Thhhhhhhhhhhhhhhhhhhe mail not sent"+ex);
            }
        }
    }

    private void sendEmail(Notification n) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(n.getUser().getEmail());
        mail.setSubject(n.getTitle());
        mail.setText(n.getMessage());
        emailService.send(mail);
    }






    }
