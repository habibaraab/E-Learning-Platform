package com.spring.E_Learning.Service;


import com.spring.E_Learning.DTOs.CourseRequestDto;
import com.spring.E_Learning.DTOs.CourseResponseDto;
import com.spring.E_Learning.DTOs.Mapper.CourseMapper;
import com.spring.E_Learning.Enum.CourseStatus;
import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Model.Course;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.CourseRepository;
import com.spring.E_Learning.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper mapper;
    private final UserRepository userRepository;

    public CourseResponseDto createCourse(CourseRequestDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User loggedInUser = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));

        if (loggedInUser.getRole() != Role.TEACHER) {
            throw new AccessDeniedException("Only teachers can create courses");
        }

        Course course = mapper.toEntity(dto);
        course.setTeacher(loggedInUser);
        course.setStatus(CourseStatus.PENDING);

        Course savedCourse = courseRepository.save(course);
        return mapper.toResponseDto(savedCourse);
    }

    public CourseResponseDto updateCourse(int courseId, CourseRequestDto dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User loggedInUser = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        boolean isOwner = course.getTeacher().getId() == loggedInUser.getId();
        boolean isAdmin = loggedInUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You are not allowed to update this course");
        }

        if (dto.getTitle() != null) {
            course.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            course.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            course.setPrice(dto.getPrice());
        }
        if (dto.getTeacherId() != null) {
            if (!isAdmin) {
                throw new AccessDeniedException("Only admin can change course teacher");
            }
            User newTeacher = userRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + dto.getTeacherId()));
            if (newTeacher.getRole() != Role.TEACHER) {
                throw new IllegalArgumentException("User is not a TEACHER");
            }
            course.setTeacher(newTeacher);
        }

        Course saved = courseRepository.save(course);
        return mapper.toResponseDto(saved);
    }






    public List<CourseResponseDto> getAllCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User loggedInUser = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));

        if (loggedInUser.getRole() != Role.TEACHER) {
            throw new AccessDeniedException("Only teachers can list their courses");
        }

        List<Course> myCourses = courseRepository.findByTeacherId(loggedInUser.getId());
        return mapper.toDtoList(myCourses);
    }


    public void deleteCourse(int courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User loggedInUser = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));

        if (loggedInUser.getRole() != Role.TEACHER) {
            throw new AccessDeniedException("Only teachers can delete courses");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        if (course.getTeacher().getId() != loggedInUser.getId()) {
            throw new AccessDeniedException("You are not allowed to delete another teacher's course");
        }

        courseRepository.delete(course);
    }

}


