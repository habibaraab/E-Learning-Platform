package com.spring.E_Learning.Service;



import com.spring.E_Learning.DTOs.PaymentDto;

import com.spring.E_Learning.Enum.CourseStatus;
import com.spring.E_Learning.Enum.PaymentStatus;
import com.spring.E_Learning.Model.Course;
import com.spring.E_Learning.Model.Enrollment;
import com.spring.E_Learning.Model.Payment;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.CourseRepository;
import com.spring.E_Learning.Repository.EnrollmentRepository;
import com.spring.E_Learning.Repository.PaymentRepository;
import com.spring.E_Learning.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;

    @Transactional
    public PaymentDto createPayment(PaymentDto dto) {
        User student = userRepo.findById(dto.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (dto.getAmount().compareTo(course.getPrice()) < 0) {
            throw new IllegalArgumentException(
                    "Paid amount is LESS than course price. Course price: " + course.getPrice()
            );
        }

        if (dto.getAmount().compareTo(course.getPrice()) > 0) {
            throw new IllegalArgumentException(
                    "Paid amount is GREATER than course price. Course price: " + course.getPrice()
            );
        }


        boolean alreadyEnrolled = enrollmentRepo.existsByStudentIdAndCourseId(student.getId(), course.getId());
        if (alreadyEnrolled) {
            throw new IllegalStateException("Student already enrolled in this course");
        }

        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new IllegalStateException("Course is not activated by admin yet");
        }
        Payment payment = Payment.builder()
                .amount(dto.getAmount())
                .status(PaymentStatus.SUCCESS)
                .student(student)
                .course(course)
                .createdAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepo.save(payment);

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .enrollmentDate(LocalDateTime.now())
                .build();
        enrollmentRepo.save(enrollment);

        dto.setId(saved.getId());
        dto.setStatus(saved.getStatus());
        dto.setCreatedAt(saved.getCreatedAt());
        return dto;
    }

    public List<PaymentDto> getPayments() {
        User userDetails = (User)
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        int currentStudentId = userDetails.getId();

        return paymentRepo.findByStudentId(currentStudentId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    private PaymentDto mapToDto(Payment p) {
        PaymentDto dto = new PaymentDto();
        dto.setId(p.getId());
        dto.setAmount(p.getAmount());
        dto.setStatus(p.getStatus());
        dto.setCourseId(p.getCourse().getId());
        dto.setStudentId(p.getStudent().getId());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }

}
