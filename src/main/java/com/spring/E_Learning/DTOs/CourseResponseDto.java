package com.spring.E_Learning.DTOs;


import lombok.Data;

@Data
public class CourseResponseDto {
    private int id;
    private String title;
    private String description;
    private String teacherName;
}