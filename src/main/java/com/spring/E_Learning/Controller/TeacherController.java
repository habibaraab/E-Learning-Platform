package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.*;
import com.spring.E_Learning.Enum.RequestStatus;
import com.spring.E_Learning.Service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private QuestionService questionService;

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
    public ResponseEntity<ExamResponseDto> createExam(@Valid @RequestBody ExamRequestDto dto) {
        return ResponseEntity.ok(examService.createExam(dto));
    }

    @GetMapping("/GetExamsOfCourse/{courseId}")
    public ResponseEntity<List<ExamResponseDto>> getAllExams(@PathVariable int courseId) {
        return ResponseEntity.ok(examService.getExamsByCourse(courseId));
    }

    @PutMapping("/updateExam/{examId}")
    public ResponseEntity<ExamResponseDto> updateExam(
            @PathVariable int examId,
            @Valid @RequestBody ExamRequestDto dto) {
        return ResponseEntity.ok(examService.updateExam(examId, dto));
    }


    @GetMapping("/ExamDetails/{examId}")
    public ResponseEntity<ExamResponseDto> getExamById(@PathVariable int examId) {
        return ResponseEntity.ok(examService.getExamById(examId));
    }

    @DeleteMapping("/DeleteExam/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable int examId) {
        examService.deleteExam(examId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exams/{examId}/results")
    public ResponseEntity<List<ExamSubmissionResponse>> getExamResults(
            @PathVariable int examId) {
        return ResponseEntity.ok(examSubmissionService.getExamResults(examId));
    }

    //Questions
    @PostMapping("/{examId}/AddQuestions")
    public ResponseEntity<ExamResponseDto> addQuestion(
            @PathVariable int examId,
            @Valid @RequestBody QuestionDto questionDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(questionService.addQuestion(examId, questionDto));
    }

    @PutMapping("/{examId}/UdateQuestions/{questionId}")
    public ResponseEntity<ExamResponseDto> updateQuestion(
            @PathVariable int examId,
            @PathVariable int questionId,
            @Valid @RequestBody QuestionDto dto) {
        return ResponseEntity.ok(questionService.updateQuestion(examId, questionId, dto));
    }


    @DeleteMapping("/{examId}/DeleteQuestions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable int examId,
            @PathVariable int questionId) {
        questionService.deleteQuestion(examId, questionId);
        return ResponseEntity.noContent().build();
    }


    

    //AnswerOfExam
    @PutMapping("/{examId}/updateQuestionOptions/{questionId}")
    public ResponseEntity<List<OptionDto>> updateQuestionOptions(
            @PathVariable int examId,
            @PathVariable int questionId,
            @RequestBody List<OptionDto> options
    ) {
        List<OptionDto> updated = questionService.updateQuestionOptions(examId, questionId, options);
        return ResponseEntity.ok(updated);
    }







// Request
    @PutMapping("/requestStatus/{request_id}")
    public ResponseEntity<StudentRequestDto> updateStatus(
            @PathVariable int request_id,
            @RequestParam RequestStatus status) {
        return ResponseEntity.ok(studentRequestService.updateStatus(request_id, status));
    }





}
