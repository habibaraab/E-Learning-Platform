package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.DashboardStatsDto;
import com.spring.E_Learning.Service.AdminService;
import com.spring.E_Learning.Service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Executors;

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

    @GetMapping( path = "/report/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
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

}
