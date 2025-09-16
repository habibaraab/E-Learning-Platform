package com.spring.E_Learning.Model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String message;

    @Builder.Default
    private boolean  sent = false; ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    private LocalDateTime createdAt= LocalDateTime.now();
}
