package com.spring.E_Learning.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class ExamResponseDto {
    private int id;
    private String title;
    private String courseTitle;
    private List<QuestionDto> questions;
}