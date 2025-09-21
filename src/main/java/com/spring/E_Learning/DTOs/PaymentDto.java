package com.spring.E_Learning.DTOs;


import com.spring.E_Learning.Enum.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private int id;
    private BigDecimal amount;
    private PaymentStatus status;
    private int studentId;
    private int courseId;
    private LocalDateTime createdAt;
}
