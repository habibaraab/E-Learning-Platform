package com.spring.E_Learning.DTOs;


import com.spring.E_Learning.Enum.RequestStatus;
import com.spring.E_Learning.Enum.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentRequestDto {
    private Integer id;
    private Integer studentId;
    private RequestType type;
    private Integer courseId;
    private Integer examId;
    private RequestStatus status;
}
