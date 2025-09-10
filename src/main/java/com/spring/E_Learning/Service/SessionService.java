package com.spring.E_Learning.Service;


import com.spring.E_Learning.DTOs.Mapper.SessionMapper;
import com.spring.E_Learning.DTOs.SessionRequestDto;
import com.spring.E_Learning.DTOs.SessionResponseDto;
import com.spring.E_Learning.Model.Course;
import com.spring.E_Learning.Model.Session;
import com.spring.E_Learning.Repository.CourseRepository;
import com.spring.E_Learning.Repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;
    private final SessionMapper sessionMapper;


    public SessionResponseDto addSession(SessionRequestDto dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Session session = sessionMapper.toEntity(dto);
        session.setCourse(course);

        Session savedSession = sessionRepository.save(session);
        return sessionMapper.toDto(savedSession);
    }

    public List<SessionResponseDto> getSessionsByCourse(int courseId) {
        if (courseRepository.existsById(courseId)) {
           return sessionRepository.findByCourseId(courseId)
                    .stream().map(sessionMapper::toDto)
                    .collect(Collectors.toList());
        }else
            throw new EntityNotFoundException(" Not found Sessions for course " + courseId);
    }
}
