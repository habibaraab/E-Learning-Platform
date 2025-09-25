package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.*;
import com.spring.E_Learning.Service.CourseService;
import com.spring.E_Learning.Service.ExamSubmissionService;
import com.spring.E_Learning.Service.PaymentService;
import com.spring.E_Learning.Service.StudentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final ExamSubmissionService examSubmissionService;
    private final StudentRequestService studentRequestService;
    private final CourseService courseService;

    private final PaymentService paymentService;

    @PostMapping("/paymentCourse")
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto dto) {
        return ResponseEntity.ok(paymentService.createPayment(dto));
    }

    @GetMapping("/getMyCourses")
    public ResponseEntity<List<PaymentDto>> getPayments() {
        return ResponseEntity.ok(paymentService.getPayments());
    }



    @PostMapping("/sumbitExam")
    public ResponseEntity<ExamSubmissionResponse>sumbitExam(@RequestBody ExamSubmissionRequest examSubmissionRequest) {

        return ResponseEntity.ok(examSubmissionService.submitExam( examSubmissionRequest));
    }


    @GetMapping("/{studentId}/resultsOfExams")
    public ResponseEntity<List<ExamSubmissionResponse>> getStudentResults(
            @PathVariable int studentId) {
        return ResponseEntity.ok(examSubmissionService.getStudentResults(studentId));
    }

    @PostMapping("/requestOfExam")
    public ResponseEntity<StudentRequestDto>createRequest(@RequestBody StudentRequestDto studentRequestDto) {
        return ResponseEntity.ok(studentRequestService.createRequest(studentRequestDto));
    }


    @GetMapping("/getRequestStatusOfExam/{studentId}")
    public ResponseEntity<List<StudentRequestDto>> getRequests(@PathVariable int id) {
        return ResponseEntity.ok(studentRequestService.getStudentRequests(id));
    }


    @GetMapping("/getCourseDetails/{courseId}")
    public ResponseEntity<CourseResponseDto>getCourseDetails(@PathVariable int courseId) {
        return ResponseEntity.ok(courseService.getCourseDetail(courseId));
    }


    @GetMapping("/sessionOfCourse/{courseId}")
    public ResponseEntity<List<SessionResponseDto>> getSessionsByCourse(@PathVariable int courseId) {
        List<SessionResponseDto> sessions =
                courseService.getSessionsForEnrolledCourse(courseId);
        return ResponseEntity.ok(sessions);
    }

}

