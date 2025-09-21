package com.spring.E_Learning.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data

public class CourseRequestDto {

    @NotBlank(message = "title is mandatory")
    private String title;
    private String description;
    private BigDecimal price;
    private int teacherId;
}
