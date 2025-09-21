package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.ExamSubmissionRequest;
import com.spring.E_Learning.DTOs.ExamSubmissionResponse;
import com.spring.E_Learning.DTOs.PaymentDto;
import com.spring.E_Learning.DTOs.StudentRequestDto;
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

    private final PaymentService paymentService;

    @PostMapping("/payment")
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto dto) {
        return ResponseEntity.ok(paymentService.createPayment(dto));
    }
    @GetMapping("/getCourses")
    public ResponseEntity<List<PaymentDto>> getPayments() {
        return ResponseEntity.ok(paymentService.getPayments());
    }


    @PostMapping("/sumbit")
    public ResponseEntity<ExamSubmissionResponse>sumbitExam(@RequestBody ExamSubmissionRequest examSubmissionRequest) {

        return ResponseEntity.ok(examSubmissionService.submitExam( examSubmissionRequest));
    }

    @GetMapping("/{studentId}/results")
    public ResponseEntity<List<ExamSubmissionResponse>> getStudentResults(
            @PathVariable int studentId) {
        return ResponseEntity.ok(examSubmissionService.getStudentResults(studentId));
    }

    @PostMapping("/request")
    public ResponseEntity<StudentRequestDto>createRequest(@RequestBody StudentRequestDto studentRequestDto) {
        return ResponseEntity.ok(studentRequestService.createRequest(studentRequestDto));
    }


    @GetMapping("/getRequestStatus/{id}")
    public ResponseEntity<List<StudentRequestDto>> getRequests(@PathVariable int id) {
        return ResponseEntity.ok(studentRequestService.getStudentRequests(id));
    }

}

