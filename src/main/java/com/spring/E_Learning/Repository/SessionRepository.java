package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {
}
