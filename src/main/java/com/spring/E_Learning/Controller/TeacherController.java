package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.*;
import com.spring.E_Learning.Enum.RequestStatus;
import com.spring.E_Learning.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final CourseService courseService;
    private final SessionService sessionService;
    private final FileUploadService fileUploadService;
    private final ExamService examService;
    private final ExamSubmissionService examSubmissionService;
    private final StudentRequestService studentRequestService;

    //Courses

    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody CourseRequestDto dto) {
        return ResponseEntity.ok(courseService.createCourse(dto));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id ){
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }


    // Sessions
    @PostMapping(value= "/sessions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SessionResponseDto> createSession(
            @RequestParam String title,
            @RequestParam int courseId,
            @RequestParam MultipartFile videoUrl) {
        String VideoUrl = fileUploadService.uploadVideo(videoUrl);
        SessionRequestDto dto = new SessionRequestDto();
        dto.setTitle(title);
        dto.setCourseId(courseId);
        dto.setVideoUrl(VideoUrl);
        return ResponseEntity.ok(sessionService.addSession(dto));
    }

    @GetMapping("/sessions/Course/{id}")
    public ResponseEntity<List<SessionResponseDto>> getAllSessions(@PathVariable int id) {
        return ResponseEntity.ok(sessionService.getSessionsByCourse(id));
    }



    //Exams
    @PostMapping("/exams")
    public ResponseEntity<ExamResponseDto> createExam(@RequestBody ExamRequestDto dto) {
        return ResponseEntity.ok(examService.createExam(dto));
    }

    @GetMapping("/exams/Course/{id}")
    public ResponseEntity<List<ExamResponseDto>> getAllExams(@PathVariable int id) {
        return ResponseEntity.ok(examService.getExamsByCourse(id));
    }

    @GetMapping("/exams/{examId}/results")
    public ResponseEntity<List<ExamSubmissionResponse>> getExamResults(
            @PathVariable int examId) {
        return ResponseEntity.ok(examSubmissionService.getExamResults(examId));
    }


    @PutMapping("/{request_id}/status")
    public ResponseEntity<StudentRequestDto> updateStatus(
            @PathVariable int request_id,
            @RequestParam RequestStatus status) {
        return ResponseEntity.ok(studentRequestService.updateStatus(request_id, status));
    }





}
