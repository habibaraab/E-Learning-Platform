package com.spring.E_Learning.Service;

import com.spring.E_Learning.DTOs.NotificationDto;
import com.spring.E_Learning.DTOs.StudentRequestDto;
import com.spring.E_Learning.Enum.RequestStatus;
import com.spring.E_Learning.Enum.RequestType;
import com.spring.E_Learning.Model.Course;
import com.spring.E_Learning.Model.Exam;
import com.spring.E_Learning.Model.StudentRequest;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentRequestService {
    private final StudentRequestRepository studentRequestRepository;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final NotificationScheduler notificationService;

//    public StudentRequestDto createRequest(StudentRequestDto dto) {
//        User student = userRepository.findById(dto.getStudentId())
//                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
//
//        Course course = courseRepository.findById(dto.getCourseId())
//                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
//
//        Exam exam = null;
//        if (dto.getType() == RequestType.RETAKE_EXAM) {
//            exam = examRepository.findById(dto.getExamId())
//                    .orElseThrow(() -> new EntityNotFoundException("Exam not found"));
//        }
//
//        StudentRequest request = StudentRequest.builder()
//                .student(student)
//                .type(dto.getType())
//                .course(course)
//                .exam(exam)
//                .status(RequestStatus.PENDING)
//                .build();
//
//        StudentRequest saved = studentRequestRepository.save(request);
//
//        dto.setId(saved.getId());
//        dto.setStatus(saved.getStatus());
//        return dto;
//    }

    public StudentRequestDto createRequest(StudentRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User student = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        if (!"STUDENT".equalsIgnoreCase(student.getRole().name())) {
            throw new RuntimeException("Only students can send requests");
        }

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Exam exam = null;
        if (dto.getType() == RequestType.RETAKE_EXAM) {
            exam = examRepository.findById(dto.getExamId())
                    .orElseThrow(() -> new EntityNotFoundException("Exam not found"));
        }

        StudentRequest request = StudentRequest.builder()
                .student(student)
                .type(dto.getType())
                .course(course)
                .exam(exam)
                .status(RequestStatus.PENDING)
                .build();

        StudentRequest saved = studentRequestRepository.save(request);

        StudentRequestDto response = new StudentRequestDto();
        response.setId(saved.getId());
        response.setStudentId(student.getId());
        response.setCourseId(course.getId());
        response.setExamId(exam != null ? exam.getId() : null);
        response.setType(saved.getType());
        response.setStatus(saved.getStatus());

        return response;
    }



    public StudentRequestDto updateStatus(int requestId, RequestStatus status) {
        StudentRequest request = studentRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        request.setStatus(status);
        StudentRequest updated = studentRequestRepository.save(request);

        NotificationDto notif = NotificationDto.builder()
                .title("Exam Request Update")
                .message(status == RequestStatus.APPROVED
                        ? "Your exam retake request has been APPROVED."
                        : "Your exam retake request has been REJECTED.")
                .userId(updated.getStudent().getId())
                .createdAt(LocalDateTime.now())
                .read(true)
                .build();
        notificationService.createNotification(notif);

        StudentRequestDto dto = new StudentRequestDto();
        dto.setId(updated.getId());
        dto.setStudentId(updated.getStudent().getId());
        dto.setCourseId(updated.getCourse().getId());
        dto.setType(updated.getType());
        dto.setExamId(updated.getExam() != null ? updated.getExam().getId() : null);
        dto.setStatus(updated.getStatus());
        return dto;

    }

    public List<StudentRequestDto> getStudentRequests(int studentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User loggedInUser = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged in user not found"));

        if (loggedInUser.getId() != studentId) {
            throw new RuntimeException("You are not allowed to view other student's requests");
        }

        return studentRequestRepository.findByStudentId(studentId)
                .stream()
                .map(r -> {
                    StudentRequestDto dto = new StudentRequestDto();
                    dto.setId(r.getId());
                    dto.setStudentId(r.getStudent().getId());
                    dto.setCourseId(r.getCourse().getId());
                    dto.setType(r.getType());
                    dto.setExamId(r.getExam() != null ? r.getExam().getId() : null);
                    dto.setStatus(r.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }


}

