package com.spring.E_Learning.DTOs;

import lombok.Data;

@Data
public class SessionRequestDto {
    private String title;
    private String videoUrl;
    private int courseId;
}
