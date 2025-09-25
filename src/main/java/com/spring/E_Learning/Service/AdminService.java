package com.spring.E_Learning.Service;


import com.spring.E_Learning.Enum.CourseStatus;
import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Model.Course;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.CourseRepository;
import com.spring.E_Learning.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public void enableTeacher(int teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("User is not a teacher");
        }

        teacher.setActiva(true);
        userRepository.save(teacher);
    }

    public void disableUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setActiva(false);
        userRepository.save(user);
    }

    public void deleteUser(int userId) {
       if (!userRepository.existsById(userId)) {
           throw new EntityNotFoundException("User not found");
       }
       userRepository.deleteById(userId);
    }

    public void activateCourse(int courseId) {

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException("Course not found"));
        course.setStatus(CourseStatus.ACTIVE);
        courseRepository.save(course);
    }

    public void rejectCourse(int courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException("Course not found"));
        course.setStatus(CourseStatus.REJECTED);
        courseRepository.save(course);

    }
}

