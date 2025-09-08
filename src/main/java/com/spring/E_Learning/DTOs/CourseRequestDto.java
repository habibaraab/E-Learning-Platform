package com.spring.E_Learning.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class CourseRequestDto {

    @NotBlank(message = "title is mandatory")
    private String title;
    private String description;
    private int teacherId;
}
