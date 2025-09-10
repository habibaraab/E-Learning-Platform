package com.spring.E_Learning.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class ExamRequestDto {
    private String title;
    private int courseId;
    private List<QuestionDto> questions;
}

