package com.spring.E_Learning.Service;

import com.spring.E_Learning.DTOs.ExamRequestDto;
import com.spring.E_Learning.DTOs.ExamResponseDto;
import com.spring.E_Learning.DTOs.Mapper.ExamMapper;
import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Model.Course;
import com.spring.E_Learning.Model.Exam;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.CourseRepository;
import com.spring.E_Learning.Repository.ExamRepository;
import com.spring.E_Learning.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final ExamMapper examMapper;
    private final UserRepository userRepository;



    public ExamResponseDto createExam(ExamRequestDto dto) {
        User loggedInUser = getLoggedInUser();

        if (loggedInUser.getRole() != Role.TEACHER) {
            throw new AccessDeniedException("Only teachers can create exams");
        }

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Course not found with id " + dto.getCourseId()));

       if (course.getTeacher().getId() != loggedInUser.getId()) {
            throw new AccessDeniedException("You can only create exams for your own courses");
        }

        Exam exam = examMapper.toEntity(dto);
        exam.setCourse(course);
        exam.getQuestions().forEach(q -> {
            q.setExam(exam);
            q.getOptions().forEach(o -> o.setQuestion(q));
        });

        Exam savedExam = examRepository.save(exam);
        return examMapper.toDto(savedExam);
    }

    public List<ExamResponseDto> getExamsByCourse(int courseId) {
        User loggedInUser = getLoggedInUser();

        if (loggedInUser.getRole() != Role.TEACHER) {
            throw new AccessDeniedException("Only teachers can view their exams");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Course not found with id " + courseId));

        if (course.getTeacher().getId() != loggedInUser.getId()) {
            throw new AccessDeniedException("You can only view exams of your own courses");
        }

        return examRepository.findByCourseId(courseId)
                .stream()
                .map(examMapper::toDto)
                .collect(Collectors.toList());
    }

    public ExamResponseDto updateExam(int examId, ExamRequestDto dto) {
        User loggedInUser = getLoggedInUser();
        if (loggedInUser.getRole() != Role.TEACHER) {
            throw new AccessDeniedException("Only teachers can update exams");
        }
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found with id " + examId));

        if (exam.getCourse().getTeacher().getId() != loggedInUser.getId()) {
            throw new AccessDeniedException("You can only update exams of your own courses");
        }

        exam.setTitle(dto.getTitle());

        Exam savedExam = examRepository.save(exam);
        return examMapper.toDto(savedExam);
    }


    public void deleteExam(int examId) {
        User loggedInUser = getLoggedInUser();
        if (loggedInUser.getRole() != Role.TEACHER) {
            throw new AccessDeniedException("Only teachers can delete exams");
        }

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found with id " + examId));

        if (exam.getCourse().getTeacher().getId() != loggedInUser.getId()) {
            throw new AccessDeniedException("You can only delete exams of your own courses");
        }

        examRepository.delete(exam);
    }

    public ExamResponseDto getExamById(int examId) {
        User loggedInUser = getLoggedInUser();
        if (loggedInUser.getRole() != Role.TEACHER) {
            throw new AccessDeniedException("Only teachers can view exam details");
        }

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found with id " + examId));

        if (exam.getCourse().getTeacher().getId() != loggedInUser.getId()) {
            throw new AccessDeniedException("You can only view exams of your own courses");
        }

        return examMapper.toDto(exam);
    }

    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));
    }
}
