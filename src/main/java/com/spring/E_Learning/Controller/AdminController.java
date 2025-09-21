package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.ReportDto;
import com.spring.E_Learning.Service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ReportService reportService;


    @GetMapping("/report")
    public ResponseEntity<ReportDto>getReport(){
        return  ResponseEntity.ok(reportService.getDashboardReport());
    }
}
