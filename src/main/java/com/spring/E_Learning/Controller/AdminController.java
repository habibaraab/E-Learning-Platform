package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.DashboardStatsDto;
import com.spring.E_Learning.Enum.CourseStatus;
import com.spring.E_Learning.Service.AdminService;
import com.spring.E_Learning.Service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Executors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ReportService reportService;
    private final AdminService adminService;
    private final SimpMessagingTemplate messagingTemplate;

    //SSE using Emitter for Live Dashboard
    @GetMapping( path = "/stream/Dashboard", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamStats() {
        SseEmitter emitter = new SseEmitter();

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    DashboardStatsDto stats = reportService.getDashboardReport();
                    emitter.send(stats);
                    Thread.sleep(3000);
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }



    //WebSocket for LiveDashboard
//    @Scheduled(fixedRate = 3000)
//    public void broadcastStats() {
//        DashboardStatsDto stats = reportService.getDashboardReport();
//        messagingTemplate.convertAndSend("/topic/dashboard", stats);
//    }

    @PatchMapping("/teachers/{teacherId}/enableTeacherAccount")
    public ResponseEntity<String> enableTeacher(@PathVariable int teacherId) {
        adminService.enableTeacher(teacherId);
        return ResponseEntity.ok("Teacher account activated");
    }

    @PatchMapping("/teachers/{teacherId}/disableTeacherAccount")
    public ResponseEntity<String> disableUser(@PathVariable int userId,@RequestBody String message) {
        adminService.rejectTeacher(userId,message);
        return ResponseEntity.ok("User disabled");
    }






    @PutMapping("/{courseId}/activateCourse")
    public ResponseEntity<String> activateCourse(@PathVariable int courseId) {
        adminService.activateCourse(courseId);
        return ResponseEntity.ok("Course activated");
    }

    @PutMapping("/{courseId}/rejectCourse")
    public ResponseEntity<String> rejectCourse(@PathVariable int courseId,@RequestBody String message) {
        adminService.rejectCourse(courseId,message);
        return ResponseEntity.ok("Course rejected");
    }


    @DeleteMapping("/DeleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok("User deleted");
    }





}
