package com.spring.E_Learning.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private Long totalStudents;
    private Long totalTeachers;
    private Long totalCourses;
    private Long totalPayments;
    private Long totalExams;
    private Long pendingRequests;
}
