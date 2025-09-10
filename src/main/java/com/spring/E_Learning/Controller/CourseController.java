package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.CourseRequestDto;
import com.spring.E_Learning.DTOs.CourseResponseDto;
import com.spring.E_Learning.DTOs.SessionRequestDto;
import com.spring.E_Learning.DTOs.SessionResponseDto;
import com.spring.E_Learning.Service.CourseService;
import com.spring.E_Learning.Service.FileUploadService;
import com.spring.E_Learning.Service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final SessionService sessionService;
    private final FileUploadService fileUploadService;

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

    @GetMapping("/sessions/{id}")
    public ResponseEntity<List<SessionResponseDto>> getAllSessions(@PathVariable int id) {
        return ResponseEntity.ok(sessionService.getSessionsByCourse(id));
    }


}
