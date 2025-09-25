package com.spring.E_Learning.Service;


import com.spring.E_Learning.DTOs.NotificationDto;
import com.spring.E_Learning.Enum.CourseStatus;
import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Model.Course;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.CourseRepository;
import com.spring.E_Learning.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final NotificationScheduler notificationScheduler;

    public void enableTeacher(int teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("User is not a teacher");
        }

        teacher.setActiva(true);
        userRepository.save(teacher);
        NotificationDto notif = NotificationDto.builder()
                .userId(teacher.getId())
                .title("Account Activation Approved")
                .message("Your  account has been approved and activated.")
                .read(true)
                .createdAt(LocalDateTime.now())
                .build();
        notificationScheduler.createNotification(notif);
    }

    public void rejectTeacher(int teacherId, String reason) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("User is not a teacher");
        }

        teacher.setActiva(false);
        userRepository.save(teacher);

        NotificationDto notif = NotificationDto.builder()
                .userId(teacher.getId())
                .title("Account Activation Rejected")
                .message("Unfortunately, your teacher account activation has been rejected."
                        + (reason != null ? " Reason: " + reason : ""))
                .read(true)
                .build();
        notificationScheduler.createNotification(notif);
    }

    public void deleteUser(int userId) {
       if (!userRepository.existsById(userId)) {
           throw new EntityNotFoundException("User not found");
       }
       userRepository.deleteById(userId);
    }

    public void activateCourse(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        course.setStatus(CourseStatus.ACTIVE);
        courseRepository.save(course);

        NotificationDto notif = NotificationDto.builder()
                .userId(course.getTeacher().getId())
                .read(true)
                .title("Course Activation Approved")
                .message("Your course \"" + course.getTitle() + "\" has been approved and is now active.")
                .build();
        notificationScheduler.createNotification(notif);
    }

    public void rejectCourse(int courseId, String reason) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        course.setStatus(CourseStatus.REJECTED);
        courseRepository.save(course);

        NotificationDto notif = NotificationDto.builder()
                .userId(course.getTeacher().getId())
                .title("Course Activation Rejected")
                .read(true)
                .message("Your course \"" + course.getTitle() + "\" has been rejected."
                        + (reason != null ? " Reason: " + reason : ""))
                .build();
        notificationScheduler.createNotification(notif);
    }
}

