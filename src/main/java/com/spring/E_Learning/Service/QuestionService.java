package com.spring.E_Learning.Service;


import com.spring.E_Learning.DTOs.ExamResponseDto;
import com.spring.E_Learning.DTOs.Mapper.ExamMapper;
import com.spring.E_Learning.DTOs.Mapper.OptionMapper;
import com.spring.E_Learning.DTOs.OptionDto;
import com.spring.E_Learning.DTOs.QuestionDto;
import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Model.Exam;
import com.spring.E_Learning.Model.Option;
import com.spring.E_Learning.Model.Question;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final ExamMapper examMapper;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    private final OptionMapper optionMapper;

    public ExamResponseDto addQuestion(int examId, QuestionDto questionDto) {
        User logged = getLoggedInUser();

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found"));

        if (logged.getRole() != Role.TEACHER ||
                exam.getCourse().getTeacher().getId() != logged.getId()) {
            throw new AccessDeniedException("You can only modify exams of your own courses");
        }

        Question question = examMapper.toEntity(questionDto);
        question.setExam(exam);
        question.getOptions().forEach(o -> o.setQuestion(question));

        exam.getQuestions().add(question);
        Exam saved = examRepository.save(exam);

        return examMapper.toDto(saved);
    }

    public ExamResponseDto updateQuestion(int examId, int questionId, QuestionDto dto) {
        User logged = getLoggedInUser();
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found"));

        if (logged.getRole() != Role.TEACHER ||
                exam.getCourse().getTeacher().getId() != logged.getId()) {
            throw new AccessDeniedException("Not allowed");
        }

        Question question = exam.getQuestions()
                .stream().filter(q -> q.getId() == questionId)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        if (dto.getText() != null)  question.setText(dto.getText());
        if (dto.getType() != null)  question.setType(dto.getType());
        if (dto.getOptions() != null) {
            question.getOptions().clear();
            dto.getOptions().forEach(oDto -> {
                Option option = examMapper.toEntity(oDto);
                option.setQuestion(question);
                question.getOptions().add(option);
            });
        }

        Exam saved = examRepository.save(exam);
        return examMapper.toDto(saved);
    }

    public void deleteQuestion(int examId, int questionId) {
        User logged = getLoggedInUser();
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found"));

        if (logged.getRole() != Role.TEACHER ||
                exam.getCourse().getTeacher().getId() != logged.getId()) {
            throw new AccessDeniedException("Not allowed");
        }

        exam.getQuestions().removeIf(q -> q.getId() == questionId);
        examRepository.save(exam);
    }

    public List<OptionDto> updateQuestionOptions(
            int examId,
            int questionId,
            List<OptionDto> newOptions
    ) {
        User logged = getLoggedInUser();

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found"));

        if (logged.getRole() != Role.TEACHER ||
                exam.getCourse().getTeacher().getId() != logged.getId()) {
            throw new AccessDeniedException("You can update options only for your own exams");
        }

        Question question = exam.getQuestions()
                .stream()
                .filter(q -> q.getId() == questionId)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Question not found in this exam"));


        optionRepository.deleteAll(question.getOptions());

        List<Option> saved = new ArrayList<>();
        for (OptionDto dto : newOptions) {
            Option opt = new Option();
            opt.setText(dto.getText());
            opt.setCorrect(dto.isCorrect());
            opt.setQuestion(question);
            saved.add(optionRepository.save(opt));
        }

        return saved.stream().map(optionMapper::optionToDto).toList();
    }


    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));
    }

}
