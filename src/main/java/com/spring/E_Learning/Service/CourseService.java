package com.spring.E_Learning.Service;


import com.spring.E_Learning.DTOs.CourseRequestDto;
import com.spring.E_Learning.DTOs.CourseResponseDto;
import com.spring.E_Learning.DTOs.Mapper.CourseMapper;
import com.spring.E_Learning.Model.Course;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.CourseRepository;
import com.spring.E_Learning.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper mapper;
    private final UserRepository userRepository;

    public CourseResponseDto createCourse(CourseRequestDto dto) {
        User teacher = userRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + dto.getTeacherId()));
        Course course = mapper.toEntity(dto);
        course.setTeacher(teacher);
        Course savedCourse = courseRepository.save(course);
        return mapper.toResponseDto(savedCourse);
    }

    public List<CourseResponseDto> getAllCourses() {
        return mapper.toDtoList(courseRepository.findAll());
    }

}


