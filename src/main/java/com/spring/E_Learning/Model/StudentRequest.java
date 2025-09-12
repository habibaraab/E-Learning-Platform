package com.spring.E_Learning.Model;

import com.spring.E_Learning.Enum.RequestStatus;
import com.spring.E_Learning.Enum.RequestType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class StudentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;


    @Enumerated(EnumType.STRING)
    private RequestType type;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;



}
