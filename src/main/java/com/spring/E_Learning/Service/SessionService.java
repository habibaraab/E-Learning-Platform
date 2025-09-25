package com.spring.E_Learning.Service;


import com.spring.E_Learning.DTOs.Mapper.SessionMapper;
import com.spring.E_Learning.DTOs.SessionRequestDto;
import com.spring.E_Learning.DTOs.SessionResponseDto;
import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Model.Course;
import com.spring.E_Learning.Model.Session;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.CourseRepository;
import com.spring.E_Learning.Repository.EnrollmentRepository;
import com.spring.E_Learning.Repository.SessionRepository;
import com.spring.E_Learning.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;
    private final SessionMapper sessionMapper;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public SessionResponseDto addSession(SessionRequestDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User loggedInUser = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (loggedInUser.getRole() != Role.TEACHER ||
                course.getTeacher().getId() != loggedInUser.getId()) {
            throw new AccessDeniedException("You can only add sessions to your own courses");
        }

        Session session = sessionMapper.toEntity(dto);
        session.setCourse(course);
        Session savedSession = sessionRepository.save(session);

        return sessionMapper.toDto(savedSession);
    }

    public List<SessionResponseDto> getAllSessionsByCourse(int courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User loggedInUser = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (loggedInUser.getRole() != Role.TEACHER ||
                course.getTeacher().getId() != loggedInUser.getId()) {
            throw new AccessDeniedException("You can only view sessions of your own courses");
        }

        return sessionRepository.findByCourseId(courseId)
                .stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }


    public SessionResponseDto getSession(int sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));


        if (user.getRole() == Role.TEACHER) {
            if (session.getCourse().getTeacher().getId() != user.getId()) {
                throw new AccessDeniedException("Not your course");
            }
        } else if (user.getRole() == Role.STUDENT) {
            if (!enrollmentRepository.existsByStudentIdAndCourseId(user.getId(),
                    session.getCourse().getId())) {
                throw new AccessDeniedException("Not enrolled");
            }
        } else {
            throw new AccessDeniedException("Unauthorized");
        }
        return sessionMapper.toDto(session);
    }

    public SessionResponseDto updateSession(int id, SessionRequestDto dto) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User teacher = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));

        if (teacher.getRole() != Role.TEACHER ||
                session.getCourse().getTeacher().getId() != teacher.getId()) {
            throw new AccessDeniedException("You can update only your sessions");
        }
        if (dto.getTitle() != null) session.setTitle(dto.getTitle());
        if (dto.getVideoUrl() != null) session.setVideoUrl(dto.getVideoUrl());

        return sessionMapper.toDto(sessionRepository.save(session));
    }


    public void deleteSession(int id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User teacher = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));
        if (teacher.getRole() != Role.TEACHER ||
                session.getCourse().getTeacher().getId() != teacher.getId()) {
            throw new AccessDeniedException("You can delete only your sessions");
        }
        sessionRepository.delete(session);
    }



}
