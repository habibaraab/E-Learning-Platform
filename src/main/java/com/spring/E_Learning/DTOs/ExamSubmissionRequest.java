package com.spring.E_Learning.DTOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamSubmissionRequest {
    private int examId;
    private List<ExamAnswerDto> answers;
}
