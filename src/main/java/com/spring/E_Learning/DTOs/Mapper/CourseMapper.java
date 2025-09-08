package com.spring.E_Learning.DTOs.Mapper;

import com.spring.E_Learning.DTOs.CourseRequestDto;
import com.spring.E_Learning.DTOs.CourseResponseDto;
import com.spring.E_Learning.Model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CourseMapper {


    CourseRequestDto toDto(Course entity);
    List<CourseResponseDto> toDtoList(List<Course> courses);

    @Mapping(source = "teacher.name", target = "teacherName")
    CourseResponseDto toResponseDto(Course course);
    Course toEntity(CourseRequestDto dto);


}
