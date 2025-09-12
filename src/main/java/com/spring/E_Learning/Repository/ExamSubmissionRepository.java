package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Model.ExamSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamSubmissionRepository extends JpaRepository<ExamSubmission, Integer> {
    List<ExamSubmission> findByExamId(int examId);
    List<ExamSubmission> findByStudentId(int studentId);
    Boolean existsByExamIdAndStudentId(int studentId, int examId);
}
