package com.spring.E_Learning.Model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String title;

    private String description;


    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Session> sessions = new HashSet<>();


    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private Set<Exam>exams = new HashSet<>();

}
