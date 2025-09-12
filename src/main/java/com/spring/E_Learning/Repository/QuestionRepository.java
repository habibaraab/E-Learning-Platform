package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
