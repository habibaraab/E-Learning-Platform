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

    @PostMapping("/addCourse")
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody CourseRequestDto dto) {
        return ResponseEntity.ok(courseService.createCourse(dto));
    }

    @GetMapping("/myCourses")
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }


    @PatchMapping("/UpdateCourse/{courseId}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @PathVariable int courseId,
            @RequestBody CourseRequestDto dto) {
        return ResponseEntity.ok(courseService.updateCourse(courseId, dto));
    }

    @DeleteMapping("/DeleteCourse/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int courseId ){
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }






    // Sessions
    @PostMapping(value= "/addSessions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @GetMapping("/AllSessionsOfCourse/{courseId}")
    public ResponseEntity<List<SessionResponseDto>> getAllSessions(@PathVariable int courseId) {
        return ResponseEntity.ok(sessionService.getAllSessionsByCourse(courseId));
    }


    @GetMapping("/getSession/{sessionId}")
    public SessionResponseDto getSession(@PathVariable int sessionId) {
        return sessionService.getSession(sessionId);
    }


    @PatchMapping("/updateSession/{sessionId}")
    public SessionResponseDto updateSession(@PathVariable int sessionId,
                                            @RequestBody SessionRequestDto dto) {
        return sessionService.updateSession(sessionId, dto);
    }

    @DeleteMapping("/DeleteSession/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable int sessionId) {
        sessionService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }





    //Exams
    @PostMapping("/addExams")
    public ResponseEntity<ExamResponseDto> createExam(@RequestBody ExamRequestDto dto) {
        return ResponseEntity.ok(examService.createExam(dto));
    }

    @GetMapping("/examsOfCourse/{courseId}")
    public ResponseEntity<List<ExamResponseDto>> getAllExams(@PathVariable int courseId) {
        return ResponseEntity.ok(examService.getExamsByCourse(courseId));
    }

    @GetMapping("/exams/{examId}/results")
    public ResponseEntity<List<ExamSubmissionResponse>> getExamResults(
            @PathVariable int examId) {
        return ResponseEntity.ok(examSubmissionService.getExamResults(examId));
    }


    


    @PutMapping("/requestStatus/{request_id}")
    public ResponseEntity<StudentRequestDto> updateStatus(
            @PathVariable int request_id,
            @RequestParam RequestStatus status) {
        return ResponseEntity.ok(studentRequestService.updateStatus(request_id, status));
    }





}
