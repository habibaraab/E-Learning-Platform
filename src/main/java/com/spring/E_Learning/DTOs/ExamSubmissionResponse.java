package com.spring.E_Learning.DTOs;


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
public class ExamSubmissionResponse {
    private int submissionId;
    private int score;
    private LocalDateTime submittedAt;
}
