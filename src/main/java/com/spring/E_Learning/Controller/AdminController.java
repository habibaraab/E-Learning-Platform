package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.ReportDto;
import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.CourseRepository;
import com.spring.E_Learning.Repository.EnrollmentRepository;
import com.spring.E_Learning.Repository.PaymentRepository;
import com.spring.E_Learning.Repository.UserRepository;
import com.spring.E_Learning.Service.AdminService;
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
    private final AdminService adminService;

    @PutMapping("/teachers/{teacherId}/enable")
    public ResponseEntity<String> enableTeacher(@PathVariable int teacherId) {
        adminService.enableTeacher(teacherId);
        return ResponseEntity.ok("Teacher account activated");
    }

    @PutMapping("/users/{userId}/disable")
    public ResponseEntity<String> disableUser(@PathVariable int userId) {
        adminService.disableUser(userId);
        return ResponseEntity.ok("User disabled");
    }


    @GetMapping("/report")
    public ResponseEntity<ReportDto>getReport(){
        return  ResponseEntity.ok(reportService.getDashboardReport());
    }


}
