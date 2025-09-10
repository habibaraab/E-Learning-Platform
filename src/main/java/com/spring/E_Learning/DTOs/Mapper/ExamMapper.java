package com.spring.E_Learning.DTOs.Mapper;


import com.spring.E_Learning.DTOs.ExamRequestDto;
import com.spring.E_Learning.DTOs.ExamResponseDto;
import com.spring.E_Learning.DTOs.OptionDto;
import com.spring.E_Learning.DTOs.QuestionDto;
import com.spring.E_Learning.Model.Exam;
import com.spring.E_Learning.Model.Option;
import com.spring.E_Learning.Model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExamMapper {
    @Mapping(source = "course.title", target = "courseTitle")
    ExamResponseDto toDto(Exam exam);

    @Mapping(target = "course", ignore = true)
    Exam toEntity(ExamRequestDto dto);

    QuestionDto toDto(Question question);
    Question toEntity(QuestionDto dto);

    OptionDto toDto(Option option);
    Option toEntity(OptionDto dto);
}
