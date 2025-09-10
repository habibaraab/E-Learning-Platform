package com.spring.E_Learning.Service;

import com.spring.E_Learning.DTOs.ExamRequestDto;
import com.spring.E_Learning.DTOs.ExamResponseDto;
import com.spring.E_Learning.DTOs.Mapper.ExamMapper;
import com.spring.E_Learning.Model.Course;
import com.spring.E_Learning.Model.Exam;
import com.spring.E_Learning.Repository.CourseRepository;
import com.spring.E_Learning.Repository.ExamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final ExamMapper examMapper;

    public ExamResponseDto createExam(ExamRequestDto dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id "+dto.getCourseId()));

        Exam exam = examMapper.toEntity(dto);
        exam.setCourse(course);

        // Ensure bidirectional relationship
        exam.getQuestions().forEach(q -> {
            q.setExam(exam);
            q.getOptions().forEach(o -> o.setQuestion(q));
        });

        Exam savedExam = examRepository.save(exam);
        return examMapper.toDto(savedExam);
    }

    public List<ExamResponseDto> getExamsByCourse(int courseId) {
        return examRepository.findByCourseId(courseId)
                .stream().map(examMapper::toDto)
                .collect(Collectors.toList());
    }
}
