package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.ExamSubmissionRequest;
import com.spring.E_Learning.DTOs.ExamSubmissionResponse;
import com.spring.E_Learning.Service.ExamSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final ExamSubmissionService examSubmissionService;

    @PostMapping("/{id}/sumbit")
    public ResponseEntity<ExamSubmissionResponse>sumbitExam(@PathVariable int id,@RequestBody ExamSubmissionRequest examSubmissionRequest) {

        return ResponseEntity.ok(examSubmissionService.submitExam(id, examSubmissionRequest));
    }

    @GetMapping("/{studentId}/results")
    public ResponseEntity<List<ExamSubmissionResponse>> getStudentResults(
            @PathVariable int studentId) {
        return ResponseEntity.ok(examSubmissionService.getStudentResults(studentId));
    }


}

