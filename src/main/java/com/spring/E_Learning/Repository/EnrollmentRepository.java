package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByStudentId(int studentId);
    List<Enrollment> findByCourseId(int courseId);
    boolean existsByStudentIdAndCourseId(int  studentId, int courseId);

}
