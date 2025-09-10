package com.spring.E_Learning.Repository;

import com.spring.E_Learning.DTOs.SessionResponseDto;
import com.spring.E_Learning.Model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    List<Session>findByCourseId(int courseId);
}
