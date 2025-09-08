package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
}
