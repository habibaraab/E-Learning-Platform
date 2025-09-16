package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification>findBySentFalse();
}
