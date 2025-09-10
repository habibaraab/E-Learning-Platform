package com.spring.E_Learning.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private int id;
    private String text;
    private String type;
    private List<OptionDto> options;

}
