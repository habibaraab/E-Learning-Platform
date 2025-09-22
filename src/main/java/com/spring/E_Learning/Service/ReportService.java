package com.spring.E_Learning.Service;


import com.spring.E_Learning.DTOs.DashboardStatsDto;
import com.spring.E_Learning.Enum.RequestStatus;
import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PaymentRepository paymentRepository;
    private final ExamRepository examRepository;
    private final StudentRequestRepository studentRequestRepository;

    public DashboardStatsDto getDashboardReport() {
        Long totalStudents = userRepository.countByRole(Role.STUDENT);
        Long totalTeachers = userRepository.countByRole(Role.TEACHER);
        Long totalCourses = courseRepository.count();
        Long totalPayments = paymentRepository.count();
        Long totalExams = examRepository.count();
        Long  pendingRequests = studentRequestRepository.countByStatus(RequestStatus.PENDING);

        return new DashboardStatsDto(
                totalStudents,
                totalTeachers,
                totalCourses,
                totalPayments,
                totalExams,
                pendingRequests
        );
    }



}
