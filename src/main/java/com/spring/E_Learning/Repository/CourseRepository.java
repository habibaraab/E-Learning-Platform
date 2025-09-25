package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByTeacherId(Integer teacherId);

}
