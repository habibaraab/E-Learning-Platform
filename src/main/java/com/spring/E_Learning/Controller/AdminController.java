package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.ReportDto;
import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.CourseRepository;
import com.spring.E_Learning.Repository.EnrollmentRepository;
import com.spring.E_Learning.Repository.PaymentRepository;
import com.spring.E_Learning.Repository.UserRepository;
import com.spring.E_Learning.Service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ReportService reportService;


        private final UserRepository userRepository;
        private final CourseRepository courseRepository;
        private final PaymentRepository paymentRepository;
        private final EnrollmentRepository enrollmentRepository;

        @PutMapping("/teachers/{teacherId}/enable")
        public ResponseEntity<String> enableTeacher(@PathVariable int teacherId) {
            User teacher = userRepository.findById(teacherId)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

            if (teacher.getRole() != Role.TEACHER) {
                throw new IllegalArgumentException("User is not a teacher");
            }

            teacher.setActiva(true);
            userRepository.save(teacher);
            return ResponseEntity.ok("Teacher account activated");
        }

    @GetMapping("/report")
    public ResponseEntity<ReportDto>getReport(){
        return  ResponseEntity.ok(reportService.getDashboardReport());
    }


    @PutMapping("/users/{userId}/disable")
    public ResponseEntity<String> disableUser(@PathVariable int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setActiva(false);
        userRepository.save(user);
        return ResponseEntity.ok("User disabled");
    }
}
