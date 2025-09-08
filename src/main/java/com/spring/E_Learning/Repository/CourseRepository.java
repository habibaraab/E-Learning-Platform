package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
