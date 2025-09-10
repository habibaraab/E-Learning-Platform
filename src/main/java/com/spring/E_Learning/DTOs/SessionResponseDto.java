package com.spring.E_Learning.DTOs;


import lombok.Data;

@Data
public class SessionResponseDto {
    private int id;
    private String title;
    private String videoUrl;
    private String courseTitle;

}

