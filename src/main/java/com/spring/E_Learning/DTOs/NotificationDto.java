package com.spring.E_Learning.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
    private int id;
    private String title;
    private String message;
    private boolean read;
    private Integer userId;
    private LocalDateTime createdAt;
}
